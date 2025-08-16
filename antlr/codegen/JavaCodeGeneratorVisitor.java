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
    
    // --- NOVO: Variável para controlar a indentação ---
    private int indentLevel = 0;

    // --- NOVO: Contador para gerar nomes de variáveis de loop únicos ---
    private int loopCounter = 0; 

    public JavaCodeGeneratorVisitor(String className) {
        this.className = className;
    }

    // --- NOVO: Método auxiliar para gerar a indentação ---
    private String indent() {
        return "    ".repeat(indentLevel);
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

    @Override
    public String visitCmdIterate(CmdIterate cmd) {
        StringBuilder sb = new StringBuilder();
        
        if (cmd.condition instanceof ItCondExpr) {
            // Lógica para iterate(expr) - já implementada
            String loopVar = "_i" + loopCounter++;
            String limitExpr = cmd.condition.accept(this);
            sb.append("for (int ").append(loopVar).append(" = 0; ")
              .append(loopVar).append(" < ").append(limitExpr).append("; ")
              .append(loopVar).append("++) {\n");
            sb.append(cmd.body.accept(this));
            sb.append(indent()).append("}\n");
            
        } else if (cmd.condition instanceof ItCondLabelled) {
            // --- NOVO: Lógica para iterate(id : expr) ---
            ItCondLabelled cond = (ItCondLabelled) cmd.condition;
            String loopVar = cond.label;
            String limitExpr = cond.expression.accept(this);
            
            // Adiciona a variável do loop ao escopo para que não seja redeclarada dentro do bloco
            declaredVariables.add(loopVar);
            // Assume que é um Int por enquanto
            variableTypes.put(loopVar, new Type("Int", 0)); 
            
            sb.append("for (int ").append(loopVar).append(" = 0; ")
              .append(loopVar).append(" < ").append(limitExpr).append("; ")
              .append(loopVar).append("++) {\n");
              
            sb.append(cmd.body.accept(this));
            
            sb.append(indent()).append("}\n");
            
            // Remove a variável do loop do escopo para que possa ser usada novamente fora
            declaredVariables.remove(loopVar);
            variableTypes.remove(loopVar);
        }
        
        return sb.toString();
    }

    // --- NOVO: Visitor para a condição com label ---
    @Override
    public String visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return itCondLabelled.expression.accept(this);
    }

    // --- NOVO: Implementação do visitor para a condição do iterate ---
    @Override
    public String visitItCondExpr(ItCondExpr itCondExpr) {
        // Apenas visita a expressão interna
        return itCondExpr.expression.accept(this);
    }

    @Override
    public String visitExpVar(ExpVar exp) {
        return exp.name;
    }
    
    @Override
    public String visitProg(Prog prog) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.util.Scanner;\n\n"); // Adiciona import
        sb.append("public class ").append(className).append(" {\n");
        indentLevel++;
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                sb.append(def.accept(this));
            }
        }
        indentLevel--;
        sb.append("}\n");
        return sb.toString();
    }

    // ATUALIZADO: Cria a instância do Scanner no início do main
    @Override
    public String visitFun(Fun fun) {
        if (fun.id.equals("main")) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent()).append("public static void main(String[] args) {\n");
            
            indentLevel++;
            sb.append(indent()).append("Scanner _scanner = new Scanner(System.in);\n"); // Cria o Scanner
            indentLevel--;

            declaredVariables.clear();
            variableTypes.clear();
            
            sb.append(fun.body.accept(this));
            
            sb.append(indent()).append("}\n");
            return sb.toString();
        }
        return "";
    }

    // --- NOVO: Implementação do Comando Read ---
    @Override
    public String visitCmdRead(CmdRead cmd) {
        StringBuilder sb = new StringBuilder();
        // Apenas para LValueId por enquanto
        if (cmd.lvalue instanceof LValueId) {
            String varName = ((LValueId) cmd.lvalue).id;
            
            sb.append("System.out.print(\" entrada> \");\n");
            
            // Adiciona indentação para a próxima linha
            sb.append(indent());

            // Assumimos que a variável já foi declarada e pegamos seu tipo.
            Type varType = variableTypes.get(varName);
            if (varType == null) {
                throw new IllegalStateException("Variável '" + varName + "' usada em 'read' sem ter sido declarada.");
            }
            
            // Escolhe o método do Scanner com base no tipo
            switch (varType.baseType) {
                case "Int":
                    sb.append(varName).append(" = _scanner.nextInt();\n");
                    break;
                case "Float":
                    sb.append(varName).append(" = _scanner.nextFloat();\n");
                    break;
                // Por padrão, lemos como String para Char ou outros tipos
                default:
                    sb.append(varName).append(" = _scanner.next();\n");
                    break;
            }
        } else {
            return "// read em arrays/campos ainda não suportado\n";
        }
        return sb.toString();
    }
    
    // ATUALIZADO: Gerencia a indentação para todos os comandos dentro dele.
    @Override
    public String visitCmdBlock(CmdBlock cmd) {
        StringBuilder sb = new StringBuilder();
        indentLevel++;
        for (Cmd c : cmd.cmds) {
            sb.append(indent()).append(c.accept(this));
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visitCmdPrint(CmdPrint cmd) {
        String value = cmd.value.accept(this);
        // Se for um caractere de nova linha, usa println(), senão usa print().
        if (cmd.value instanceof ExpChar && ((ExpChar) cmd.value).value == '\n') {
            return "System.out.println();\n";
        }
        return "System.out.print(" + value + ");\n";
    }
    
    // --- NOVO: Implementação do CmdIf com a nova estratégia de indentação ---
    @Override
    public String visitCmdIf(CmdIf cmd) {
        StringBuilder sb = new StringBuilder();
        String condition = cmd.condition.accept(this);

        sb.append("if (").append(condition).append(") {\n");
        
        // O corpo do 'then' (que pode ser um bloco ou comando único) é visitado.
        // Se for um bloco, visitCmdBlock cuidará da indentação interna.
        sb.append(cmd.thenBranch.accept(this));
        
        sb.append(indent()).append("}");

        if (cmd.elseBranch != null) {
            sb.append(" else {\n");
            sb.append(cmd.elseBranch.accept(this));
            sb.append(indent()).append("}");
        }
        sb.append("\n");
        return sb.toString();
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
        char c = exp.value;
        switch (c) {
            case '\n':
                return "\"\\n\""; // Retorna a string Java para nova linha
            case '\t':
                return "\"\\t\""; // Retorna a string Java para tabulação
            case '\\':
                return "\"\\\\\""; // Retorna a string Java para a própria barra
            // Adicione outros caracteres de escape se necessário
            default:
                // Para caracteres normais, apenas os coloca entre aspas duplas
                return "\"" + c + "\"";
        }
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

    // --- NOVO: Implementação dos Operadores Unários ---
    @Override
    public String visitExpUnaryOp(ExpUnaryOp exp) {
        String operand = exp.exp.accept(this);
        // A tradução é direta, apenas colocamos o operador na frente
        // e garantimos os parênteses para a precedência correta.
        return "(" + exp.op + operand + ")";
    }

    // --- Métodos não implementados (mantidos como no seu arquivo) ---
    @Override
    public String visitCmd(Cmd exp) { return null; }
    @Override
    public String visitCmdCall(CmdCall cmd) { return null; }
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
    public String visitItCond(ItCond itCond) { return null; }
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