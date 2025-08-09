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

    public JasminGeneratorVisitor(String className) {
        this.className = className;
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

    // --- Métodos de Visita ---

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

        // Visita todas as definições (funções e dados)
        for (Def def : prog.definitions) {
            def.accept(this);
        }
        return null;
    }

    @Override
    public Void visitFun(Fun fun) {
        locals.clear();
        nextLocalIndex = 0;

        String signature = "main([Ljava/lang/String;)V";

        emitHeader(".method public static " + signature);

        emit(".limit stack 20");
        emit(".limit locals 20");

        emitHeader("");

        fun.body.accept(this);

        emit("return");
        emitHeader(".end method");
        return null;
    }

    @Override
    public Void visitCmdPrint(CmdPrint cmd) {
        emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
        cmd.value.accept(this);

        Type typeToPrint = cmd.value.expType;
        String descriptor = "";
        if (typeToPrint.baseType.equals("Int") || typeToPrint.baseType.equals("Bool")) {
            descriptor = "I"; // Inteiro ou Booleano (0/1)
        } else if (typeToPrint.baseType.equals("Float")) {
            descriptor = "F"; // Float
        } else if (typeToPrint.baseType.equals("Char")) {
            descriptor = "C"; // Char
        } else {
            descriptor = "Ljava/lang/String;";
        }

        emit("invokevirtual java/io/PrintStream/println(" + descriptor + ")V");
        return null;
    }

    @Override
    public Void visitExpInt(ExpInt exp) {
        emit("ldc " + exp.value);
        return null;
    }

    @Override
    public Void visitCmdAssign(CmdAssign cmd) {
        cmd.expression.accept(this);

        if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            int localIndex;

            if (!locals.containsKey(varName)) {
                locals.put(varName, nextLocalIndex);
                localIndex = nextLocalIndex;
                nextLocalIndex++;
            } else {
                localIndex = locals.get(varName);
            }

            Type varType = cmd.expression.expType;
            if (varType.baseType.equals("Int") || varType.baseType.equals("Bool") || varType.baseType.equals("Char")) {
                emit("istore " + localIndex);
            } else if (varType.baseType.equals("Float")) {
                emit("fstore " + localIndex);
            } else {
                emit("astore " + localIndex);
            }
        } else {
            // TODO: Implementar atribuição para campos de 'data' e índices de array
        }
        return null;
    }

    @Override
    public Void visitExpVar(ExpVar expVar) {
        if (!locals.containsKey(expVar.name)) {
            throw new RuntimeException(
                    "Erro do gerador: variável '" + expVar.name + "' não encontrada no mapa de locais.");
        }
        int localIndex = locals.get(expVar.name);

        if (expVar.expType.baseType.equals("Int") || expVar.expType.baseType.equals("Bool")
                || expVar.expType.baseType.equals("Char")) {
            emit("iload " + localIndex);
        } else if (expVar.expType.baseType.equals("Float")) {
            emit("fload " + localIndex);
        } else {
            emit("aload " + localIndex);
        }
        return null;
    }

    // --- Placeholders para os métodos restantes ---

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
        /* TODO */ return null;
    }

    // Adicione estes métodos à sua classe jasmin/JasminGeneratorVisitor.java

    /**
     * @brief Gera código para o comando 'if' (com ou sem 'else').
     *        Usa saltos condicionais para controlar o fluxo de execução.
     */
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
        /* TODO */ return null;
    }

    @Override
    public Void visitCmdRead(CmdRead cmd) {
        emit("new java/util/Scanner");
        emit("dup");
        emit("getstatic java/lang/System/in Ljava/io/InputStream;");
        emit("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V");

        // pilha.
        emit("invokevirtual java/util/Scanner/nextInt()I");

        if (cmd.lvalue instanceof LValueId) {
            String varName = ((LValueId) cmd.lvalue).id;
            if (!locals.containsKey(varName)) {
                throw new RuntimeException(
                        "Erro do gerador: variável '" + varName + "' do 'read' não foi previamente declarada.");
            }
            int localIndex = locals.get(varName);
            emit("istore " + localIndex);
        } else {
            // TODO: Implementar 'read' para campos e arrays se necessário
        }

        return null;
    }

    @Override
    public Void visitCmdReturn(CmdReturn cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitData(Data data) {
        /* TODO (gerar campos estáticos ou classes internas se necessário) */ return null;
    }

    @Override
    public Void visitDataAbstract(DataAbstract data) {
        /* TODO */ return null;
    }

    @Override
    public Void visitDataRegular(DataRegular data) {
        /* TODO */ return null;
    }

    @Override
    public Void visitDecl(Decl decl) {
        return null;
    } // Geralmente não gera código diretamente

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
        /* TODO */ return null;
    }

    @Override
    public Void visitExpCall(ExpCall exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpCallIndexed(ExpCallIndexed exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpChar(ExpChar exp) {
        emit("ldc " + (int) exp.value);
        return null;
    }

    @Override
    public Void visitExpField(ExpField exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpFloat(ExpFloat exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpIndex(ExpIndex exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpNew(ExpNew exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpNull(ExpNull exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpParen(ExpParen exp) {
        exp.exp.accept(this);
        return null;
    }

    @Override
    public Void visitExpUnaryOp(ExpUnaryOp exp) {
        /* TODO */ return null;
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
        /* TODO */ return null;
    }

    @Override
    public Void visitLValue(LValue lValue) {
        lValue.accept(this);
        return null;
    }

    @Override
    public Void visitLValueField(LValueField lValueField) {
        /* TODO */ return null;
    }

    @Override
    public Void visitLValueId(LValueId lValueId) {
        /* TODO */ return null;
    }

    @Override
    public Void visitLValueIndex(LValueIndex lValueIndex) {
        /* TODO */ return null;
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