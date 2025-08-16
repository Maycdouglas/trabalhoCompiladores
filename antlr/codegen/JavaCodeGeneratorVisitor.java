// /antlr/codegen/JavaCodeGeneratorVisitor.java
package codegen;

import ast.*;
import interpreter.Visitor;
import java.util.HashSet;
import java.util.Set;

public class JavaCodeGeneratorVisitor implements Visitor<String> {

    private final String className;
    // CORREÇÃO: A variável foi movida para ser um campo da classe.
    private final Set<String> declaredVariables = new HashSet<>();

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

     // --- NOVO: Implementação da Atribuição (CmdAssign) ---
    @Override
    public String visitCmdAssign(CmdAssign cmd) {
        // Só trataremos LValueId por enquanto (atribuição a variáveis simples como 'x')
        if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            String expression = cmd.expression.accept(this);

            // Verifica se a variável já foi declarada
            if (!declaredVariables.contains(varName)) {
                // Se não foi, declara-a. O tipo é pego da expressão à direita.
                declaredVariables.add(varName);
                String javaType = getJavaType(cmd.expression.expType);
                return javaType + " " + varName + " = " + expression + ";\n";
            } else {
                // Se já foi, apenas atribui o novo valor.
                return varName + " = " + expression + ";\n";
            }
        }
        return "// Atribuição não suportada ainda\n";
    }

    // --- NOVO: Implementação de Variáveis em Expressões ---
    @Override
    public String visitExpVar(ExpVar exp) {
        // Quando uma variável é usada, simplesmente retornamos seu nome.
        return exp.name;
    }
    
    // --- NOVO: Helper para mapear tipos de 'lang' para Java ---
    private String getJavaType(Type langType) {
        if (langType.arrayDim > 0) {
            // Lógica para arrays (será implementada depois)
            return getJavaType(new Type(langType.baseType, 0)) + "[]".repeat(langType.arrayDim);
        }
        switch (langType.baseType) {
            case "Int":
                return "int";
            case "Float":
                return "float";
            case "Bool":
                return "boolean";
            case "Char":
                // Em Java, literais de string são com aspas duplas. Vamos usar String por simplicidade.
                return "String"; 
            default:
                // Para tipos de dados definidos pelo usuário (futuramente)
                return langType.baseType;
        }
    }
    
    // Expressões simples
    @Override
    public String visitExpInt(ExpInt exp) {
        return String.valueOf(exp.value);
    }

    @Override
    public String visitExpChar(ExpChar exp) {
        // Para consistência, trataremos todos os literais de texto como String em Java
        return "\"" + exp.value + "\"";
    }
    
    @Override
    public String visitExpBool(ExpBool exp) {
        return String.valueOf(exp.value);
    }
    
    // --- Métodos não implementados (ainda) ---
    // Deixe-os retornando `null` ou uma string vazia por enquanto.

    @Override
    public String visitCmd(Cmd exp) { return null; }
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