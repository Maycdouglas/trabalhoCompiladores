/* Feito por:

 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC

 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C

*/

package jasmin;

import ast.*;
import interpreter.Visitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @brief Visitor responsável por percorrer a AST e gerar código de montagem
 *        Jasmin.
 */
public class JasminGeneratorVisitor implements Visitor<Void> {

    private String className;
    private StringBuilder code = new StringBuilder();
    private int labelCounter = 0;

    // Mapeia nomes de variáveis para seus índices na JVM (locais)
    private Map<String, Integer> locals = new HashMap<>();
    private int nextLocalIndex = 0;
    private final Map<String, Data> delta;
    private final Map<String, Fun> theta;
    private boolean isInsideData = false;

    public JasminGeneratorVisitor(String className, Map<String, Data> delta, Map<String, Fun> theta) {
        this.className = className;
        this.delta = delta;
        this.theta = theta;
    }

    /**
     * @brief Retorna o código Jasmin gerado.
     */
    public String getCode() {
        return code.toString();
    }

    /**
     * @brief Adiciona uma instrução ao buffer de código.
     */
    private void emit(String instruction) {
        code.append("    ").append(instruction).append("\n");
    }

    /**
     * @brief Adiciona o cabeçalho de um método ao buffer de código.
     */
    private void emitHeader(String header) {
        code.append(header).append("\n");
    }

    /**
     * @brief Adiciona um rótulo ao buffer de código.
     */
    private void emitLabel(String label) {
        code.append(label).append(":\n");
    }

    /**
     * @brief Gera um novo nome de rótulo único.
     */
    private String newLabel() {
        return "L" + (labelCounter++);
    }

    @Override
    public Void visitProg(Prog prog) {
        emitHeader(".class public " + className);
        emitHeader(".super java/lang/Object");
        emitHeader("");

        emitHeader("; Construtor padrão");
        emitHeader(".method public <init>()V");
        emit("aload_0");
        emit("invokenonvirtual java/lang/Object/<init>()V");
        emit("return");
        emitHeader(".end method");
        emitHeader("");

        for (Def def : prog.definitions) {
            def.accept(this);
        }
        return null;
    }

    @Override
    public Void visitFun(Fun fun) {
        locals.clear();
        nextLocalIndex = 0;

        if (isInsideData) {
            locals.put("this", 0);
            nextLocalIndex = 1;
        }

        StringBuilder signature = new StringBuilder();
        signature.append(fun.id).append("(");

        for (Param p : fun.params) {
            signature.append(getJasminType(p.type));
            locals.put(p.id, nextLocalIndex);

            nextLocalIndex++;
        }
        signature.append(")");

        if (fun.retTypes.isEmpty()) {
            signature.append("V");
        } else {
            signature.append(getJasminType(fun.retTypes.get(0)));
        }

        if (fun.id.equals("main")) {
            signature = new StringBuilder("main([Ljava/lang/String;)V");
            nextLocalIndex = 1;
        }

        if (isInsideData) {
            emitHeader(".method public " + signature.toString());
        } else {
            if (fun.id.equals("main")) {
                signature = new StringBuilder("main([Ljava/lang/String;)V");
                nextLocalIndex = 1; // 'args' do main
            }
            emitHeader(".method public static " + signature.toString());
        }
        emit(".limit stack 20");
        emit(".limit locals 20");
        emitHeader("");

        fun.body.accept(this);

        if (fun.retTypes.isEmpty()) {
            emit("return");
        }

        emitHeader(".end method");
        return null;
    }

    @Override
    public Void visitCmdPrint(CmdPrint cmd) {
        if (cmd.value instanceof ExpChar && ((ExpChar) cmd.value).value == '\n') {
            emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
            emit("invokevirtual java/io/PrintStream/println()V");
            return null;
        }

        emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
        cmd.value.accept(this);

        Type typeToPrint = cmd.value.expType;
        String descriptor = "";
        if (typeToPrint.baseType.equals("Int")) {
            descriptor = "I";
        } else if (typeToPrint.baseType.equals("Float")) {
            descriptor = "F";
        } else if (typeToPrint.baseType.equals("Char")) {
            descriptor = "C";
        } else if (typeToPrint.baseType.equals("Bool")) {
            cmd.value.accept(this); // Empilha 0 ou 1
            String falseLabel = newLabel();
            String endLabel = newLabel();

            emit("ifeq " + falseLabel); // Se o valor for 0 (false), salta para falseLabel

            // Se for true (não saltou)
            emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
            emit("ldc \"true\""); // Carrega a string "true"
            emit("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");
            emit("goto " + endLabel);

            // Se for false
            emitLabel(falseLabel);
            emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
            emit("ldc \"false\""); // Carrega a string "false"
            emit("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");

            emitLabel(endLabel);
            
            return null;
        } else {
            descriptor = "Ljava/lang/Object;";
        }

        emit("invokevirtual java/io/PrintStream/print(" + descriptor + ")V");
        return null;
    }

    @Override
    public Void visitExpInt(ExpInt exp) {
        emit("ldc " + exp.value);
        return null;
    }

    @Override
    public Void visitCmdAssign(CmdAssign cmd) {
        // CASO 1: Atribuição a uma variável local (ex: i = 0)
        if (cmd.target instanceof LValueId) {
            LValueId target = (LValueId) cmd.target;

            // Visita a expressão para colocar o valor a ser atribuído na pilha
            cmd.expression.accept(this);

            // Obtém o índice da variável local (ou cria um novo)
            String varName = target.id;
            int localIndex = locals.computeIfAbsent(varName, k -> nextLocalIndex++);

            // Determina a instrução 'store' correta com base no tipo da expressão
            Type varType = cmd.expression.expType;
            if (varType.arrayDim > 0 || delta.containsKey(varType.baseType)) {
                // Se for um array ou um objeto, usa 'astore' (store reference)
                emit("astore " + localIndex);
            } else if (varType.baseType.equals("Float")) {
                emit("fstore " + localIndex);
            } else {
                // Para Int, Bool e Char, usa 'istore'
                emit("istore " + localIndex);
            }
        }
        // CASO 2: Atribuição a um elemento de array (ex: board[i][j] = 'A')
        else if (cmd.target instanceof LValueIndex) {
            LValueIndex lv = (LValueIndex) cmd.target;

            // 1. Empilha a referência do array e o(s) índice(s)
            // O target.accept() irá recursivamente gerar 'aaload' para obter o sub-array
            // correto.
            // ex: para board[i][j], 'lv.target.accept(this)' gera o código para obter
            // 'board[i]'
            lv.target.accept(this); // Empilha a referência do array (ou sub-array)
            lv.index.accept(this); // Empilha o índice final

            // 2. Empilha o valor a ser atribuído
            cmd.expression.accept(this);

            // 3. Emite a instrução de armazenamento (*astore) correta
            Type valueType = cmd.expression.expType;
            if (valueType.arrayDim > 0 || delta.containsKey(valueType.baseType)) {
                emit("aastore"); // Armazena uma referência (objeto ou outro array)
            } else {
                switch (valueType.baseType) {
                    case "Int":
                    case "Bool":
                        emit("iastore"); // integer array store
                        break;
                    case "Float":
                        emit("fastore"); // float array store
                        break;
                    case "Char":
                        emit("castore"); // character array store
                        break;
                    default:
                        throw new RuntimeException(
                                "Tipo de array não suportado para atribuição: " + valueType.baseType);
                }
            }
        }
        // CASO 3: Atribuição a um campo de objeto (ex: l.head = no)
        else if (cmd.target instanceof LValueField) {
            LValueField targetField = (LValueField) cmd.target;

            // 1. Carrega a referência do objeto na pilha
            targetField.target.accept(this);

            // 2. Carrega o valor a ser atribuído na pilha
            cmd.expression.accept(this);

            // 3. Obtém os metadados para a instrução 'putfield'
            Type targetType = targetField.target.expType; // Tipo do objeto (ex: LList)
            String className = targetType.baseType;
            String fieldName = targetField.field;
            Type fieldType = cmd.expression.expType; // Tipo do valor sendo atribuído
            String fieldDescriptor = getJasminType(fieldType);

            // 4. Emite a instrução para armazenar o valor no campo
            emit("putfield " + className + "/" + fieldName + " " + fieldDescriptor);
        }
        return null;
    }

    private String getJasminType(Type type) {
        if (type.arrayDim > 0) {
            // Constrói o descritor para um tipo array, ex: [I para Int[], [[C para Char[][]
            return "[".repeat(type.arrayDim) + getJasminType(new Type(type.baseType, 0));
        }
        switch (type.baseType) {
            case "Int":
                return "I";
            case "Float":
                return "F";
            case "Bool":
                return "Z"; // Descritor JVM para boolean
            case "Char":
                return "C";
            default:
                return "L" + type.baseType + ";"; // Descritor para tipos de objeto/data
        }
    }

    // Em jasmin/JasminGeneratorVisitor.java

    @Override
    public Void visitExpVar(ExpVar expVar) {
        if (locals.containsKey(expVar.name)) {
            int localIndex = locals.get(expVar.name);
            Type varType = expVar.expType;

            if (varType.arrayDim > 0 || delta.containsKey(varType.baseType)) {
                emit("aload " + localIndex);
            } else if (varType.baseType.equals("Float")) {
                emit("fload " + localIndex);
            } else { // Int, Bool, Char
                emit("iload " + localIndex);
            }
        } else if (isInsideData) {
            emit("aload_0");

            String fieldName = expVar.name;
            Type fieldType = expVar.expType; // O tipo do campo já foi determinado pelo SemanticVisitor.
            String fieldDescriptor = getJasminType(fieldType);

            emit("getfield " + this.className + "/" + fieldName + " " + fieldDescriptor);
        } else {
            throw new RuntimeException(
                    "Erro do gerador: variável '" + expVar.name + "' não encontrada no mapa de locais.");
        }
        return null;
    }

    @Override
    public Void visitCmd(Cmd exp) {
        return null;
    }

    @Override
    public Void visitCmdBlock(CmdBlock cmd) {
        for (Cmd c : cmd.cmds) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public Void visitCmdCall(CmdCall cmd) {
        Fun funDef = theta.get(cmd.id);
        if (funDef == null) {
            throw new RuntimeException("Erro do gerador: função '" + cmd.id + "' não encontrada.");
        }

        for (Exp arg : cmd.args) {
            arg.accept(this);
        }

        StringBuilder signature = new StringBuilder();
        signature.append(className).append("/").append(funDef.id).append("(");
        for (Param p : funDef.params) {
            signature.append(getJasminType(p.type));
        }
        signature.append(")");

        if (funDef.retTypes.isEmpty()) {
            signature.append("V");
        } else {
            signature.append(getJasminType(funDef.retTypes.get(0)));
        }

        emit("invokestatic " + signature.toString());

        if (!funDef.retTypes.isEmpty() && (cmd.rets == null || cmd.rets.isEmpty())) {
            emit("pop");
        }

        if (cmd.rets != null && !cmd.rets.isEmpty()) {
            LValue firstRet = cmd.rets.get(0);
            if (firstRet instanceof LValueId) {
                String varName = ((LValueId) firstRet).id;
                int localIndex = locals.get(varName);
                emit("istore " + localIndex);
            }
        }

        return null;
    }

    @Override
    public Void visitCmdIf(CmdIf cmd) {
        String elseLabel = newLabel();
        String endLabel = newLabel();

        cmd.condition.accept(this);

        // "if equal" em Jasmin
        emit("ifeq " + elseLabel);

        cmd.thenBranch.accept(this);
        emit("goto " + endLabel);

        emitLabel(elseLabel);
        if (cmd.elseBranch != null) {
            cmd.elseBranch.accept(this);
        }

        emitLabel(endLabel);

        return null;
    }

    @Override
    public Void visitCmdIterate(CmdIterate cmd) {
        // Caso 1: iterate(expr) - Pode ser um 'for' (Int) ou 'while' (Bool)
        if (cmd.condition instanceof ItCondExpr) {
            ItCondExpr cond = (ItCondExpr) cmd.condition;
            Type condType = cond.expression.expType;

            // Comportamento como "for" para tipos Int
            if (condType.baseType.equals("Int")) {
                String loopStart = newLabel();
                String loopEnd = newLabel();
                int counterIndex = nextLocalIndex++;
                int limitIndex = nextLocalIndex++;

                cond.expression.accept(this);
                emit("istore " + limitIndex);
                emit("ldc 0");
                emit("istore " + counterIndex);

                emitLabel(loopStart);
                emit("iload " + counterIndex);
                emit("iload " + limitIndex);
                emit("if_icmpge " + loopEnd);

                cmd.body.accept(this);

                emit("iinc " + counterIndex + " 1");
                emit("goto " + loopStart);
                emitLabel(loopEnd);

                // Comportamento como "while" para tipos Bool
            } else if (condType.baseType.equals("Bool")) {
                String loopStart = newLabel();
                String loopEnd = newLabel();
                emitLabel(loopStart);
                cond.expression.accept(this);
                emit("ifeq " + loopEnd);
                cmd.body.accept(this);
                emit("goto " + loopStart);
                emitLabel(loopEnd);
            }

            // Caso 2: iterate(id : expr) - Loop com uma variável de controle
        } else if (cmd.condition instanceof ItCondLabelled) {
            ItCondLabelled cond = (ItCondLabelled) cmd.condition;
            Type exprType = cond.expression.expType;

            // Comportamento "for-i" para um range de inteiros
            if (exprType.baseType.equals("Int") && exprType.arrayDim == 0) {
                String loopStart = newLabel();
                String loopEnd = newLabel();
                Integer shadowedVarIndex = locals.get(cond.label);
                int loopVarIndex = nextLocalIndex++;
                int limitIndex = nextLocalIndex++;
                locals.put(cond.label, loopVarIndex);

                cond.expression.accept(this);
                emit("istore " + limitIndex);
                emit("ldc 0");
                emit("istore " + loopVarIndex);

                emitLabel(loopStart);
                emit("iload " + loopVarIndex);
                emit("iload " + limitIndex);
                emit("if_icmpge " + loopEnd);
                cmd.body.accept(this);
                emit("iinc " + loopVarIndex + " 1");
                emit("goto " + loopStart);
                emitLabel(loopEnd);

                if (shadowedVarIndex != null) {
                    locals.put(cond.label, shadowedVarIndex);
                } else {
                    locals.remove(cond.label);
                }

                // NOVO CÓDIGO AQUI: Comportamento "foreach" para arrays
            } else if (exprType.arrayDim > 0) {
                String loopStart = newLabel();
                String loopEnd = newLabel();
                int arrayIndex = nextLocalIndex++;
                int counterIndex = nextLocalIndex++;
                int loopVarIndex = nextLocalIndex++;
                locals.put(cond.label, loopVarIndex);

                cond.expression.accept(this);
                emit("astore " + arrayIndex);

                emit("ldc 0");
                emit("istore " + counterIndex);

                emitLabel(loopStart);
                emit("iload " + counterIndex);
                emit("aload " + arrayIndex);
                emit("arraylength");
                emit("if_icmpge " + loopEnd);

                emit("aload " + arrayIndex);
                emit("iload " + counterIndex);

                Type elementType = new Type(exprType.baseType, exprType.arrayDim - 1);
                switch (elementType.baseType) {
                    case "Int":
                    case "Bool":
                        emit("iaload");
                        emit("istore " + loopVarIndex);
                        break;
                    case "Float":
                        emit("faload");
                        emit("fstore " + loopVarIndex);
                        break;
                    case "Char":
                        emit("caload");
                        emit("istore " + loopVarIndex);
                        break;
                    default:
                        emit("aaload");
                        emit("astore " + loopVarIndex);
                        break;
                }

                cmd.body.accept(this);
                emit("iinc " + counterIndex + " 1");
                emit("goto " + loopStart);
                emitLabel(loopEnd);

                locals.remove(cond.label);
            }
        }
        return null;
    }

    @Override
    public Void visitCmdRead(CmdRead cmd) {
        // LValueId: leitura para variável local
        if (cmd.lvalue instanceof LValueId) {
            String varName = ((LValueId) cmd.lvalue).id;
            if (!locals.containsKey(varName)) {
                throw new RuntimeException(
                        "Erro do gerador: variável '" + varName + "' do 'read' não foi previamente declarada.");
            }
            int localIndex = locals.get(varName);

            // decide qual método do Scanner usar a partir do tipo da variável
            if (cmd.lvalue.expType != null && cmd.lvalue.expType.baseType.equals("Float")) {
                // float
                emit("new java/util/Scanner");
                emit("dup");
                emit("getstatic java/lang/System/in Ljava/io/InputStream;");
                emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");
                emit("invokevirtual java/util/Scanner/nextFloat()F");
                emit("fstore " + localIndex);
            } else {
                // por padrão: int / bool / char tratamos como int (nextInt)
                emit("new java/util/Scanner");
                emit("dup");
                emit("getstatic java/lang/System/in Ljava/io/InputStream;");
                emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");
                emit("invokevirtual java/util/Scanner/nextInt()I");
                emit("istore " + localIndex);
            }

        } else if (cmd.lvalue instanceof LValueIndex) {
            LValueIndex target = (LValueIndex) cmd.lvalue;

            target.target.accept(this);
            target.index.accept(this);

            String elemBase = "Int";
            if (target.target != null && target.target.expType != null) {
                elemBase = target.target.expType.baseType;
            }

            if (elemBase.equals("Float")) {
                emit("new java/util/Scanner");
                emit("dup");
                emit("getstatic java/lang/System/in Ljava/io/InputStream;");
                emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");
                emit("invokevirtual java/util/Scanner/nextFloat()F");
                emit("fastore");
            } else {
                emit("new java/util/Scanner");
                emit("dup");
                emit("getstatic java/lang/System/in Ljava/io/InputStream;");
                emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");
                emit("invokevirtual java/util/Scanner/nextInt()I");
                emit("iastore");
            }

        } else {
            throw new RuntimeException("read: LValue do tipo '" + cmd.lvalue.getClass().getSimpleName()
                    + "' ainda não suportado pelo gerador.");
        }

        return null;
    }

    @Override
    public Void visitCmdReturn(CmdReturn cmd) {
        if (cmd.values.isEmpty()) {
            emit("return");
            return null;
        }

        // Pega o primeiro valor de retorno
        Exp returnValue = cmd.values.get(0);
        returnValue.accept(this);

        Type returnType = returnValue.expType;
        if (returnType.arrayDim > 0 || delta.containsKey(returnType.baseType)) {
            emit("areturn");
        } else if (returnType.baseType.equals("Float")) {
            emit("freturn");
        } else {
            emit("ireturn");
        }
        return null;
    }

    @Override
    public Void visitData(Data data) {
        data.accept(this);
        return null;
    }

    @Override
    public Void visitDataAbstract(DataAbstract data) {
        emitHeader("\n; --- Definição da classe para o tipo de dados abstrato: " + data.name + " ---");
        emitHeader(".class public " + data.name);
        emitHeader(".super java/lang/Object");
        emitHeader("");

        for (Decl decl : data.declarations) {
            String fieldName = decl.id;
            String fieldType = getJasminType(decl.type);
            emitHeader(".field public " + fieldName + " " + fieldType);
        }
        emitHeader("");

        emitHeader("; Construtor padrão");
        emitHeader(".method public <init>()V");
        emit("aload_0");
        emit("invokenonvirtual java/lang/Object/<init>()V");
        emit("return");
        emitHeader(".end method");
        emitHeader("");

        String originalClassName = this.className;
        this.className = data.name;

        this.isInsideData = true; // Entra no contexto de 'data'
        for (Fun fun : data.functions) {
            fun.accept(this);
        }
        this.isInsideData = false; // Sai do contexto de 'data'

        this.className = originalClassName;

        emitHeader("; --- Fim da definição de " + data.name + " ---\n");

        return null;
    }

    @Override
    public Void visitDataRegular(DataRegular data) {
        emitHeader("\n; --- Definição da classe para o tipo de dados: " + data.name + " ---");
        emitHeader(".class public " + data.name);
        emitHeader(".super java/lang/Object");
        emitHeader("");

        for (Decl decl : data.declarations) {
            String fieldName = decl.id;
            String fieldType = getJasminType(decl.type);
            emitHeader(".field public " + fieldName + " " + fieldType);
        }
        emitHeader("");

        emitHeader("; Construtor padrão");
        emitHeader(".method public <init>()V");
        emit("aload_0");
        emit("invokenonvirtual java/lang/Object/<init>()V");
        emit("return");
        emitHeader(".end method");
        emitHeader("; --- Fim da definição de " + data.name + " ---\n");

        return null;
    }

    @Override
    public Void visitDecl(Decl decl) {
        return null;
    }

    @Override
    public Void visitDef(Def def) {
        def.accept(this);
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        exp.accept(this);
        return null;
    }

    @Override
    public Void visitExpField(ExpField exp) {
        exp.target.accept(this);

        Type targetType = exp.target.expType;
        String className = targetType.baseType;
        Data dataDef = delta.get(className);

        if (dataDef == null) {
            throw new RuntimeException("Definição do tipo de dados '" + className + "' não encontrada.");
        }

        Type fieldType = null;
        if (dataDef instanceof DataRegular) {
            for (Decl decl : ((DataRegular) dataDef).declarations) {
                if (decl.id.equals(exp.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        } else if (dataDef instanceof DataAbstract) {
            for (Decl decl : ((DataAbstract) dataDef).declarations) {
                if (decl.id.equals(exp.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        }

        if (fieldType == null) {
            throw new RuntimeException("O tipo '" + className + "' não possui o campo '" + exp.field + "'.");
        }

        String fieldDescriptor = getJasminType(fieldType);

        emit("getfield " + className + "/" + exp.field + " " + fieldDescriptor);

        return null;
    }

    private Void handleBooleanShortCircuit(ExpBinOp exp) {
        String endLabel = newLabel();

        if (exp.op.equals("&&")) {
            String falseLabel = newLabel();
            exp.left.accept(this);
            emit("ifeq " + falseLabel);

            exp.right.accept(this);
            emit("goto " + endLabel);

            emitLabel(falseLabel);
            emit("ldc 0");

        }

        emitLabel(endLabel);
        return null;
    }

    @Override
    public Void visitExpBinOp(ExpBinOp exp) {

        if (exp.op.equals("&&")) {
            return handleBooleanShortCircuit(exp);
        }

        exp.left.accept(this);
        exp.right.accept(this);

        Type operandType = exp.left.expType;

        String instructionPrefix = "";
        if (operandType.baseType.equals("Int") || operandType.baseType.equals("Bool")
                || operandType.baseType.equals("Char")) {
            instructionPrefix = "i"; // iadd, isub, if_icmpeq
        } else if (operandType.baseType.equals("Float")) {
            instructionPrefix = "f"; // fadd, fsub, fcmpl
        }

        switch (exp.op) {
            // Operações Aritméticas
            case "+":
                emit(instructionPrefix + "add");
                break;
            case "-":
                emit(instructionPrefix + "sub");
                break;
            case "*":
                emit(instructionPrefix + "mul");
                break;
            case "/":
                emit(instructionPrefix + "div");
                break;
            case "%":
                emit(instructionPrefix + "rem");
                break;

            // Comparações
            case "==":
            case "!=":
            case "<":
                String trueLabel = newLabel();
                String endLabel = newLabel();

                if (instructionPrefix.equals("i")) {
                    switch (exp.op) {
                        case "==":
                            emit("if_icmpeq " + trueLabel);
                            break;
                        case "!=":
                            emit("if_icmpne " + trueLabel);
                            break;
                        case "<":
                            emit("if_icmplt " + trueLabel);
                            break;
                    }
                } else { // Float
                    emit("fcmpl");
                    switch (exp.op) {
                        case "==":
                            emit("ifeq " + trueLabel);
                            break;
                        case "!=":
                            emit("ifne " + trueLabel);
                            break;
                        case "<":
                            emit("iflt " + trueLabel);
                            break;
                    }
                }

                emit("ldc 0"); // Empilha o valor 0 (false).
                emit("goto " + endLabel);

                emitLabel(trueLabel);
                emit("ldc 1"); // Empilha o valor 1 (true).

                emitLabel(endLabel);
                break;

            case "&&":
            case "||":
                break;
        }
        return null;
    }

    @Override
    public Void visitExpBool(ExpBool exp) {
        if (exp.value) {
            emit("ldc 1"); // Carrega a constante inteira 1 (true)
        } else {
            emit("ldc 0"); // Carrega a constante inteira 0 (false)
        }
        return null;
    }

    @Override
    public Void visitExpCall(ExpCall exp) {
        Fun funDef = theta.get(exp.id);
        if (funDef == null) {
            throw new RuntimeException("Erro do gerador: função '" + exp.id + "' não encontrada.");
        }

        for (Exp arg : exp.args) {
            arg.accept(this);
        }

        StringBuilder signature = new StringBuilder();
        signature.append(className).append("/").append(funDef.id).append("(");
        for (Param p : funDef.params) {
            signature.append(getJasminType(p.type));
        }
        signature.append(")");

        if (funDef.retTypes.isEmpty()) {
            throw new RuntimeException("Função '" + exp.id + "' sem valor de retorno usada em uma expressão.");
        } else {
            signature.append(getJasminType(funDef.retTypes.get(0)));
        }

        emit("invokestatic " + signature.toString());

        return null;
    }

    @Override
    public Void visitExpCallIndexed(ExpCallIndexed exp) {
        Fun funDef = theta.get(exp.call.id);
        if (funDef == null) {
            throw new RuntimeException("Erro do gerador: função '" + exp.call.id + "' não encontrada.");
        }

        for (Exp arg : exp.call.args) {
            arg.accept(this);
        }

        StringBuilder signature = new StringBuilder();
        signature.append(className).append("/").append(funDef.id).append("(");
        for (Param p : funDef.params) {
            signature.append(getJasminType(p.type));
        }
        signature.append(")");
        if (funDef.retTypes.isEmpty()) {
            signature.append("V");
        } else {
            signature.append(getJasminType(funDef.retTypes.get(0)));
        }

        emit("invokestatic " + signature.toString());

        return null;
    }

    @Override
    public Void visitExpChar(ExpChar exp) {
        emit("ldc " + (int) exp.value);
        return null;
    }

    @Override
    public Void visitExpFloat(ExpFloat exp) {
        emit("ldc " + exp.value);
        return null;
    }

    // Em JasminGeneratorVisitor.java

    @Override
    public Void visitExpIndex(ExpIndex exp) {
        // 1. Carrega a referência do array/sub-array na pilha
        exp.target.accept(this);
        // 2. Carrega o valor do índice na pilha
        exp.index.accept(this);

        // 3. Determina a instrução de load correta
        Type resultType = exp.expType; // Tipo do resultado da operação e.g., Char[] ou Char

        if (resultType.arrayDim > 0 || delta.containsKey(resultType.baseType)) {
            emit("aaload");
        } else {
            switch (resultType.baseType) {
                case "Int":
                case "Bool":
                    emit("iaload");
                    break;
                case "Float":
                    emit("faload");
                    break;
                case "Char":
                    emit("caload");
                    break;
                default:
                    throw new RuntimeException("Tipo primitivo não tratado no acesso ao array: " + resultType.baseType);
            }
        }
        return null;
    }

    @Override
    public Void visitExpNew(ExpNew exp) {
        if (exp.size != null) {
            exp.size.accept(this);

            Type elementType = exp.type;
            if (elementType.arrayDim > 0) {
                emit("anewarray " + getJasminType(elementType));
            } else {
                switch (elementType.baseType) {
                    case "Int":
                        emit("newarray int");
                        break;
                    case "Float":
                        emit("newarray float");
                        break;
                    case "Bool":
                        emit("newarray boolean");
                        break;
                    case "Char":
                        emit("newarray char");
                        break;
                    default:
                        emit("anewarray " + elementType.baseType);
                        break;
                }
            }
        }
        // Caso 2: Criação de um 'data' (ex: new Ponto)
        else {
            String className = exp.type.baseType;

            // 1. Aloca memória para o objeto
            emit("new " + className);
            // 2. Duplica a referência na pilha (uma para o construtor, uma para o retorno)
            emit("dup");
            // 3. Chama o construtor padrão <init>()V
            emit("invokespecial " + className + "/<init>()V");
        }
        return null;
    }

    @Override
    public Void visitExpNull(ExpNull exp) {
        emit("aconst_null"); // Carrega uma referência nula na pilha
        return null;
    }

    @Override
    public Void visitExpParen(ExpParen exp) {
        exp.exp.accept(this);
        return null;
    }

    @Override
    public Void visitExpUnaryOp(ExpUnaryOp exp) {
        exp.exp.accept(this);
        Type expType = exp.exp.expType;

        switch (exp.op) {
            case "!":

                String trueLabel = newLabel();
                String endLabel = newLabel();
                emit("ifeq " + trueLabel);
                emit("ldc 0");
                emit("goto " + endLabel);
                emitLabel(trueLabel);
                emit("ldc 1");
                emitLabel(endLabel);
                break;
            case "-":
                if (expType.baseType.equals("Int")) {
                    emit("ineg");
                } else if (expType.baseType.equals("Float")) {
                    emit("fneg");
                }
                break;
        }
        return null;
    }

    @Override
    public Void visitItCond(ItCond itCond) {
        itCond.accept(this);
        return null;
    }

    @Override
    public Void visitItCondExpr(ItCondExpr itCondExpr) {
        itCondExpr.expression.accept(this);
        return null;
    }

    @Override
    public Void visitItCondLabelled(ItCondLabelled itCondLabelled) {
        itCondLabelled.expression.accept(this);
        return null;
    }

    @Override
    public Void visitLValue(LValue lValue) {
        lValue.accept(this);
        return null;
    }

    @Override
    public Void visitLValueField(LValueField lValueField) {
        lValueField.target.accept(this);

        Type targetType = lValueField.target.expType;
        String className = targetType.baseType;
        Data dataDef = delta.get(className);

        if (dataDef == null) {
            throw new RuntimeException("Definição do tipo de dados '" + className + "' não encontrada.");
        }

        Type fieldType = null;
        if (dataDef instanceof DataRegular) {
            for (Decl decl : ((DataRegular) dataDef).declarations) {
                if (decl.id.equals(lValueField.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        }

        if (fieldType == null) {
            throw new RuntimeException("Campo '" + lValueField.field + "' não encontrado no tipo '" + className + "'.");
        }

        String fieldDescriptor = getJasminType(fieldType);

        emit("getfield " + className + "/" + lValueField.field + " " + fieldDescriptor);

        return null;
    }

    @Override
    public Void visitLValueId(LValueId lValueId) {
        int localIndex = locals.get(lValueId.id);
        Type type = lValueId.expType;
        if (type.arrayDim > 0 || delta.containsKey(type.baseType)) {
            emit("aload " + localIndex);
        } else {
            emit("iload " + localIndex);
        }
        return null;
    }

    @Override
    public Void visitLValueIndex(LValueIndex lValueIndex) {

        lValueIndex.target.accept(this);
        lValueIndex.index.accept(this);
        emit("aaload");
        return null;
    }

    @Override
    public Void visitParam(Param param) {
        return null;
    } // Tratado em visitFun

    @Override
    public Void visitType(Type type) {
        return null;
    } // Geralmente não gera código diretamente
}