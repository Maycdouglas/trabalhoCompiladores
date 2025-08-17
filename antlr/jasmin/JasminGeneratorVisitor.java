/* Feito por:

 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC

 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C

*/

package jasmin;

import ast.*;
import interpreter.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @brief Visitor responsável por percorrer a AST e gerar código de montagem
 *        Jasmin.
 */
public class JasminGeneratorVisitor implements Visitor<Void> {

    private static final Set<String> JAVA_RESERVED_WORDS = new HashSet<>(Arrays.asList(
            "abstract", "continue", "for", "new", "switch", "assert", "default",
            "goto", "package", "synchronized", "boolean", "do", "if", "private",
            "this", "break", "double", "implements", "protected", "throw", "byte",
            "else", "import", "public", "throws", "case", "enum", "instanceof",
            "return", "transient", "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void", "class", "finally",
            "long", "strictfp", "volatile", "const", "float", "native", "super", "while"));

    private String className;
    private StringBuilder code = new StringBuilder();
    private int labelCounter = 0;
    private int localIdx = 0;
    private Fun currentFunction = null;

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

    public static String getSafeClassName(String originalName) {
        if (JAVA_RESERVED_WORDS.contains(originalName)) {
            return originalName + "Class";
        }
        return originalName;
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
        this.currentFunction = fun;

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
        } else if (fun.retTypes.size() > 1) {
            signature.append("[Ljava/lang/Object;");
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
                nextLocalIndex = 1;
            }
            emitHeader(".method public static " + signature.toString());
        }

        int limitLocals = nextLocalIndex + 10;
        int limitStack = limitLocals + 5;

        emit(".limit stack " + limitStack);
        emit(".limit locals " + limitLocals);
        emitHeader("");

        fun.body.accept(this);

        if (fun.retTypes.isEmpty()) {
            emit("return");
        }

        emitHeader(".end method");

        this.currentFunction = null;
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
            cmd.value.accept(this);
            String falseLabel = newLabel();
            String endLabel = newLabel();

            emit("ifeq " + falseLabel);

            emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
            emit("ldc \"true\"");
            emit("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");
            emit("goto " + endLabel);

            emitLabel(falseLabel);
            emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
            emit("ldc \"false\"");
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
        if (cmd.target instanceof LValueId) {
            LValueId target = (LValueId) cmd.target;

            cmd.expression.accept(this);

            String varName = target.id;
            int localIndex = locals.computeIfAbsent(varName, k -> nextLocalIndex++);

            Type varType = cmd.expression.expType;
            if (varType.arrayDim > 0 || delta.containsKey(varType.baseType)) {
                emit("astore " + localIndex);
            } else if (varType.baseType.equals("Float")) {
                emit("fstore " + localIndex);
            } else {
                emit("istore " + localIndex);
            }
        } else if (cmd.target instanceof LValueIndex) {
            LValueIndex lv = (LValueIndex) cmd.target;

            lv.target.accept(this);
            lv.index.accept(this);

            cmd.expression.accept(this);

            Type valueType = cmd.expression.expType;
            if (valueType.arrayDim > 0 || delta.containsKey(valueType.baseType)) {
                emit("aastore");
            } else {
                switch (valueType.baseType) {
                    case "Int":
                    case "Bool":
                        emit("iastore");
                        break;
                    case "Float":
                        emit("fastore");
                        break;
                    case "Char":
                        emit("castore");
                        break;
                    default:
                        throw new RuntimeException(
                                "Tipo de array não suportado para atribuição: " + valueType.baseType);
                }
            }
        } else if (cmd.target instanceof LValueField) {
            LValueField targetField = (LValueField) cmd.target;

            targetField.target.accept(this);

            cmd.expression.accept(this);

            Type targetType = targetField.target.expType;
            String className = targetType.baseType;
            String fieldName = targetField.field;
            Type fieldType = cmd.expression.expType;
            String fieldDescriptor = getJasminType(fieldType);

            emit("putfield " + className + "/" + fieldName + " " + fieldDescriptor);
        }
        return null;
    }

    private String getJasminType(Type type) {
        if (type.arrayDim > 0) {
            return "[".repeat(type.arrayDim) + getJasminType(new Type(type.baseType, 0));
        }
        switch (type.baseType) {
            case "Int":
                return "I";
            case "Float":
                return "F";
            case "Bool":
                return "Z";
            case "Char":
                return "C";
            default:
                return "L" + type.baseType + ";";
        }
    }

    @Override
    public Void visitExpVar(ExpVar expVar) {
        if (locals.containsKey(expVar.name)) {
            int localIndex = locals.get(expVar.name);
            Type varType = expVar.expType;

            if (varType.arrayDim > 0 || delta.containsKey(varType.baseType)) {
                emit("aload " + localIndex);
            } else if (varType.baseType.equals("Float")) {
                emit("fload " + localIndex);
            } else {
                emit("iload " + localIndex);
            }
        } else if (isInsideData) {
            emit("aload_0");

            String fieldName = expVar.name;
            Type fieldType = expVar.expType;
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
        if (cmd.condition instanceof ItCondExpr) {
            ItCondExpr cond = (ItCondExpr) cmd.condition;
            Type condType = cond.expression.expType;

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

        } else if (cmd.condition instanceof ItCondLabelled) {
            ItCondLabelled cond = (ItCondLabelled) cmd.condition;
            Type exprType = cond.expression.expType;

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
        if (cmd.lvalue instanceof LValueId) {
            String varName = ((LValueId) cmd.lvalue).id;
            if (!locals.containsKey(varName)) {
                throw new RuntimeException(
                        "Erro do gerador: variável '" + varName + "' do 'read' não foi previamente declarada.");
            }
            int localIndex = locals.get(varName);

            if (cmd.lvalue.expType != null && cmd.lvalue.expType.baseType.equals("Float")) {
                emit("new java/util/Scanner");
                emit("dup");
                emit("getstatic java/lang/System/in Ljava/io/InputStream;");
                emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");
                emit("invokevirtual java/util/Scanner/nextFloat()F");
                emit("fstore " + localIndex);
            } else {
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
        if (currentFunction.retTypes.size() > 1) {
            emit("    bipush " + cmd.values.size());
            emit("    anewarray java/lang/Object");

            for (int i = 0; i < cmd.values.size(); i++) {
                emit("    dup");
                emit("    bipush " + i);

                Exp valueExp = cmd.values.get(i);
                valueExp.accept(this);

                Type valueType = valueExp.expType;
                if (valueType.baseType.equals("Int")) {
                    emit("    invokestatic java/lang/Integer/valueOf(I)Ljava/lang/Integer;");
                } else if (valueType.baseType.equals("Char")) {
                    emit("    invokestatic java/lang/Character/valueOf(C)Ljava/lang/Character;");
                } else if (valueType.baseType.equals("Bool")) {
                    emit("    invokestatic java/lang/Boolean/valueOf(Z)Ljava/lang/Boolean;");
                } else if (valueType.baseType.equals("Float")) {
                    emit("    invokestatic java/lang/Float/valueOf(F)Ljava/lang/Float;");
                }

                emit("    aastore");
            }
            emit("    areturn");
        } else if (!currentFunction.retTypes.isEmpty()) {
            cmd.values.get(0).accept(this);
            Type returnType = currentFunction.retTypes.get(0);
            if (returnType.baseType.equals("Int") || returnType.baseType.equals("Char")
                    || returnType.baseType.equals("Bool")) {
                emit("    ireturn");
            } else if (returnType.baseType.equals("Float")) {
                emit("    freturn");
            } else {
                emit("    areturn");
            }
        } else {
            emit("    return");
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

        this.isInsideData = true;
        for (Fun fun : data.functions) {
            fun.accept(this);
        }
        this.isInsideData = false;

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
                } else {
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

                emit("ldc 0");
                emit("goto " + endLabel);

                emitLabel(trueLabel);
                emit("ldc 1");
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
            emit("ldc 1");
        } else {
            emit("ldc 0");
        }
        return null;
    }

    @Override
    public Void visitExpCall(ExpCall exp) {
        for (Exp arg : exp.args) {
            arg.accept(this);
        }

        Fun funDef = theta.get(exp.id);
        if (funDef == null) {
            throw new RuntimeException("Função não definida chamada: " + exp.id);
        }

        StringBuilder signature = new StringBuilder();
        signature.append("(");
        for (Param p : funDef.params) {
            signature.append(getJasminType(p.type));
        }
        signature.append(")");

        if (funDef.retTypes.isEmpty()) {
            signature.append("V");
        } else if (funDef.retTypes.size() > 1) {
            signature.append("[Ljava/lang/Object;");
        } else {
            signature.append(getJasminType(funDef.retTypes.get(0)));
        }

        emit("    invokestatic " + className + "/" + exp.id + signature.toString());

        return null;
    }

    @Override
    public Void visitExpCallIndexed(ExpCallIndexed exp) {
        exp.call.accept(this);
        exp.index.accept(this);
        emit("    aaload");

        Type returnType = exp.expType;
        if (returnType.baseType.equals("Int")) {
            emit("    checkcast java/lang/Integer");
            emit("    invokevirtual java/lang/Integer/intValue()I");
        } else if (returnType.baseType.equals("Char")) {
            emit("    checkcast java/lang/Character");
            emit("    invokevirtual java/lang/Character/charValue()C");
        } else if (returnType.baseType.equals("Bool")) {
            emit("    checkcast java/lang/Boolean");
            emit("    invokevirtual java/lang/Boolean/booleanValue()Z");
        } else if (returnType.baseType.equals("Float")) {
            emit("    checkcast java/lang/Float");
            emit("    invokevirtual java/lang/Float/floatValue()F");
        } else {
            emit("    checkcast " + getJasminType(returnType).replace(";", ""));
        }
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

    @Override
    public Void visitExpIndex(ExpIndex exp) {
        exp.target.accept(this);
        exp.index.accept(this);

        Type resultType = exp.expType;

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
        } else {
            String className = exp.type.baseType;

            emit("new " + className);
            emit("dup");
            emit("invokespecial " + className + "/<init>()V");
        }
        return null;
    }

    @Override
    public Void visitExpNull(ExpNull exp) {
        emit("aconst_null");
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
    }

    @Override
    public Void visitType(Type type) {
        return null;
    }
}