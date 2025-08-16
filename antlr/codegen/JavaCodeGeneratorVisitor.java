// /antlr/codegen/JavaCodeGeneratorVisitor.java
package codegen;

import ast.*;
import interpreter.Visitor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JavaCodeGeneratorVisitor implements Visitor<String> {

    private final String className;
    private final Set<String> declaredVariables = new HashSet<>();
    private final Map<String, Type> variableTypes = new java.util.HashMap<>();

    public JavaCodeGeneratorVisitor(String className) {
        this.className = className;
    }

    private Type inferExpressionType(Exp exp) {
        if (exp.expType != null) { // Confia no SemanticVisitor se o tipo já estiver lá
             return exp.expType;
        }
        if (exp instanceof ExpInt) return new Type("Int", 0);
        if (exp instanceof ExpFloat) return new Type("Float", 0);
        if (exp instanceof ExpChar) return new Type("Char", 0);
        if (exp instanceof ExpBool) return new Type("Bool", 0);
        
        if (exp instanceof ExpVar var) {
            if (!variableTypes.containsKey(var.name)) {
                throw new IllegalStateException("Variável '" + var.name + "' usada antes da atribuição.");
            }
            return variableTypes.get(var.name);
        }

        if (exp instanceof ExpBinOp binOp) {
            Type leftType = inferExpressionType(binOp.left);
            Type rightType = inferExpressionType(binOp.right);

            switch (binOp.op) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                    if (leftType.baseType.equals("Int") && rightType.baseType.equals("Int")) {
                        return new Type("Int", 0);
                    }
                    return new Type("Float", 0);
                case "<":
                case "==":
                case "!=":
                case "&&":
                    return new Type("Bool", 0);
                default:
                    throw new IllegalStateException("Operador binário desconhecido: " + binOp.op);
            }
        }
        
        if (exp instanceof ExpParen paren) {
            return inferExpressionType(paren.exp);
        }

        throw new UnsupportedOperationException("Não foi possível inferir o tipo para a expressão: " + exp.getClass().getName());
    }


    @Override
    public String visitCmdAssign(CmdAssign cmd) {
        if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            String expression = cmd.expression.accept(this);

            if (!declaredVariables.contains(varName)) {
                declaredVariables.add(varName);
                
                Type inferredType = inferExpressionType(cmd.expression);
                variableTypes.put(varName, inferredType);

                String javaType = getJavaType(inferredType);
                return javaType + " " + varName + " = " + expression + ";\n";
            } else {
                return varName + " = " + expression + ";\n";
            }
        }
        return "// Atribuição não suportada ainda\n";
    }

    // --- NOVO MÉTODO ADICIONADO ---
    @Override
    public String visitExpVar(ExpVar exp) {
        return exp.name;
    }
    
    // --- Restante do código (sem alterações) ---

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
        if (fun.id.equals("main")) {
            StringBuilder sb = new StringBuilder();
            sb.append("    public static void main(String[] args) {\n");
            declaredVariables.clear();
            variableTypes.clear();
            sb.append(fun.body.accept(this));
            sb.append("    }\n");
            return sb.toString();
        }
        return "";
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
    
    private String getJavaType(Type langType) {
        if (langType == null) {
            throw new IllegalStateException("Erro: Tipo da expressão é nulo.");
        }
        if (langType.arrayDim > 0) {
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
                return "String"; 
            default:
                return langType.baseType;
        }
    }
    
    @Override
    public String visitExpInt(ExpInt exp) {
        return String.valueOf(exp.value);
    }
    
    @Override
    public String visitExpFloat(ExpFloat exp) {
        return String.valueOf(exp.value);
    }

    @Override
    public String visitExpChar(ExpChar exp) {
        return "\"" + exp.value + "\"";
    }
    
    @Override
    public String visitExpBool(ExpBool exp) {
        return String.valueOf(exp.value);
    }

    @Override
    public String visitExpBinOp(ExpBinOp exp) {
        String left = exp.left.accept(this);
        String right = exp.right.accept(this);
        return "(" + left + " " + exp.op + " " + right + ")";
    }

    @Override
    public String visitExpParen(ExpParen exp) {
        return "(" + exp.exp.accept(this) + ")";
    }

    // --- Métodos não implementados ---
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
    public String visitExpCall(ExpCall exp) { return null; }
    @Override
    public String visitExpCallIndexed(ExpCallIndexed exp) { return null; }
    @Override
    public String visitExpField(ExpField exp) { return null; }
    @Override
    public String visitExpIndex(ExpIndex exp) { return null; }
    @Override
    public String visitExpNew(ExpNew exp) { return null; }
    @Override
    public String visitExpNull(ExpNull exp) { return null; }
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