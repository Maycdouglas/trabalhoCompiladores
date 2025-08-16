// /antlr/codegen/JavaCodeGeneratorVisitor.java
package codegen;

import ast.*;
import interpreter.Visitor;

public class JavaCodeGeneratorVisitor implements Visitor<String> {

    private final String className;

    public JavaCodeGeneratorVisitor(String className) {
        this.className = className;
    }

    // Método principal que inicia a geração de código
    @Override
    public String visitProg(Prog prog) {
        StringBuilder sb = new StringBuilder();
        sb.append("public class ").append(className).append(" {\n");

        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                sb.append(def.accept(this));
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visitFun(Fun fun) {
        // Por enquanto, vamos tratar apenas a função 'main'
        if (fun.id.equals("main")) {
            StringBuilder sb = new StringBuilder();
            sb.append("    public static void main(String[] args) {\n");
            sb.append(fun.body.accept(this));
            sb.append("    }\n");
            return sb.toString();
        }
        return ""; // Ignorar outras funções por enquanto
    }
    
    @Override
    public String visitCmdBlock(CmdBlock cmd) {
        StringBuilder sb = new StringBuilder();
        for (Cmd c : cmd.cmds) {
            sb.append("        ").append(c.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitCmdPrint(CmdPrint cmd) {
        String value = cmd.value.accept(this);
        return "System.out.println(" + value + ");\n";
    }
    
    // Expressões simples
    @Override
    public String visitExpInt(ExpInt exp) {
        return String.valueOf(exp.value);
    }

    @Override
    public String visitExpChar(ExpChar exp) {
        // Caracteres em Java são delimitados por aspas simples
        return "'" + exp.value + "'";
    }
    
    // --- Métodos não implementados (ainda) ---
    // Deixe-os retornando `null` ou uma string vazia por enquanto.

    @Override
    public String visitCmd(Cmd exp) { return null; }
    @Override
    public String visitCmdAssign(CmdAssign cmd) { return null; }
    @Override
    public String visitCmdCall(CmdCall cmd) { return null; }
    @Override
    public String visitCmdIf(CmdIf cmd) { return null; }
    @Override
    public String visitCmdIterate(CmdIterate cmd) { return null; }
    @Override
    public String visitCmdRead(CmdRead cmd) { return null; }
    @Override
    public String visitCmdReturn(CmdReturn cmd) { return null; }
    @Override
    public String visitData(Data data) { return null; }
    @Override
    public String visitDataAbstract(DataAbstract data) { return null; }
    @Override
    public String visitDataRegular(DataRegular data) { return null; }
    @Override
    public String visitDecl(Decl decl) { return null; }
    @Override
    public String visitDef(Def def) { return null; }
    @Override
    public String visitExp(Exp exp) { return null; }
    @Override
    public String visitExpBinOp(ExpBinOp exp) { return null; }
    @Override
    public String visitExpBool(ExpBool exp) { return null; }
    @Override
    public String visitExpCall(ExpCall exp) { return null; }
    @Override
    public String visitExpCallIndexed(ExpCallIndexed exp) { return null; }
    @Override
    public String visitExpField(ExpField exp) { return null; }
    @Override
    public String visitExpFloat(ExpFloat exp) { return null; }
    @Override
    public String visitExpIndex(ExpIndex exp) { return null; }
    @Override
    public String visitExpNew(ExpNew exp) { return null; }
    @Override
    public String visitExpNull(ExpNull exp) { return null; }
    @Override
    public String visitExpParen(ExpParen exp) { return null; }
    @Override
    public String visitExpUnaryOp(ExpUnaryOp exp) { return null; }
    @Override
    public String visitExpVar(ExpVar exp) { return null; }
    @Override
    public String visitItCond(ItCond itCond) { return null; }
    @Override
    public String visitItCondExpr(ItCondExpr itCondExpr) { return null; }
    @Override
    public String visitItCondLabelled(ItCondLabelled itCondLabelled) { return null; }
    @Override
    public String visitLValue(LValue lValue) { return null; }
    @Override
    public String visitLValueField(LValueField lValueField) { return null; }
    @Override
    public String visitLValueId(LValueId lValueId) { return null; }
    @Override
    public String visitLValueIndex(LValueIndex lValueIndex) { return null; }
    @Override
    public String visitParam(Param param) { return null; }
    @Override
    public String visitType(Type type) { return null; }
}