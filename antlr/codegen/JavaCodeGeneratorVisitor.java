// /antlr/codegen/JavaCodeGeneratorVisitor.java
package codegen;

import ast.*;
import interpreter.Visitor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

public class JavaCodeGeneratorVisitor implements Visitor<String> {

    private final String className;
    private final Set<String> declaredVariables = new HashSet<>();
    private final Map<String, Type> variableTypes = new java.util.HashMap<>();
    // --- NOVO: Rastrear a função atual para saber seus tipos de retorno ---
    private Fun currentFunction = null; 
    // --- NOVO: Rastrear todas as funções para a geração das classes de retorno ---
    private Map<String, Fun> allFunctions = new java.util.HashMap<>();
    // --- NOVO: Mapa para guardar as definições de 'data' ---
    private Map<String, Data> allDataTypes = new java.util.HashMap<>();
    
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

    // --- ATUALIZADO: inferExpressionType para lidar com 'data' ---
    private Type inferExpressionType(Exp exp) {
        if (exp.expType != null) { return exp.expType; }
        if (exp instanceof ExpInt) return new Type("Int", 0);
        if (exp instanceof ExpFloat) return new Type("Float", 0);
        if (exp instanceof ExpChar) return new Type("Char", 0);
        if (exp instanceof ExpBool) return new Type("Bool", 0);
        if (exp instanceof ExpNull) return new Type("Null", 0); // Tipo para 'null'
        
        if (exp instanceof ExpVar var) {
            if (!variableTypes.containsKey(var.name)) { throw new IllegalStateException("Variável '" + var.name + "' usada antes da atribuição."); }
            return variableTypes.get(var.name);
        }
        if (exp instanceof ExpBinOp binOp) {
            Type leftType = inferExpressionType(binOp.left);
            Type rightType = inferExpressionType(binOp.right);
            switch (binOp.op) {
                case "+": case "-": case "*": case "/": case "%":
                    if (leftType.baseType.equals("Int") && rightType.baseType.equals("Int")) { return new Type("Int", 0); }
                    return new Type("Float", 0);
                case "<": case "==": case "!=": case "&&":
                    return new Type("Bool", 0);
                default:
                    throw new IllegalStateException("Operador binário desconhecido: " + binOp.op);
            }
        }
        if (exp instanceof ExpParen paren) { return inferExpressionType(paren.exp); }
        if (exp instanceof ExpUnaryOp unaryOp) {
            Type operandType = inferExpressionType(unaryOp.exp);
            if (unaryOp.op.equals("!")) { return new Type("Bool", 0); }
            if (unaryOp.op.equals("-")) { return operandType; }
        }
        if (exp instanceof ExpNew newExp) {
            if (newExp.size != null) { return new Type(newExp.type.baseType, newExp.type.arrayDim + 1); }
            return newExp.type; // Retorna o tipo do 'data', ex: Ponto
        }
        if (exp instanceof ExpIndex indexExp) {
            Type targetType = inferExpressionType(indexExp.target);
            if (targetType.arrayDim > 0) { return new Type(targetType.baseType, targetType.arrayDim - 1); }
        }
        if (exp instanceof LValueId lval) {
             if (!variableTypes.containsKey(lval.id)) { throw new IllegalStateException("Variável '" + lval.id + "' usada antes da atribuição."); }
            return variableTypes.get(lval.id);
        }
        if (exp instanceof LValueIndex lval) {
            Type targetType = inferExpressionType(lval.target);
            if (targetType.arrayDim > 0) { return new Type(targetType.baseType, targetType.arrayDim - 1); }
        }
        if (exp instanceof ExpCall call) {
            Fun fun = allFunctions.get(call.id);
            if (fun != null && fun.retTypes.size() == 1) { return fun.retTypes.get(0); }
        }
        if (exp instanceof ExpCallIndexed callIdx) {
            Fun fun = allFunctions.get(callIdx.call.id);
            if (fun != null && callIdx.index instanceof ExpInt) {
                int idx = ((ExpInt)callIdx.index).value;
                if (idx >= 0 && idx < fun.retTypes.size()) { return fun.retTypes.get(idx); }
            }
        }
        // --- NOVO: Inferência para acesso a campos ---
        if (exp instanceof ExpField fieldExp) {
            Type targetType = inferExpressionType(fieldExp.target);
            Data dataType = allDataTypes.get(targetType.baseType);
            if (dataType instanceof DataRegular) {
                for (Decl decl : ((DataRegular) dataType).declarations) {
                    if (decl.id.equals(fieldExp.field)) {
                        return decl.type;
                    }
                }
            }
            throw new IllegalStateException("Campo '" + fieldExp.field + "' não encontrado no tipo '" + targetType.baseType + "'.");
        }
        
        throw new UnsupportedOperationException("Não foi possível inferir o tipo para a expressão: " + exp.getClass().getName());
    }

    // --- NOVO: Implementação da criação de arrays (new) ---
    @Override
    public String visitExpNew(ExpNew exp) {
        if (exp.size != null) { // Verifica se é a criação de um array (ex: new Int[10])
            String size = exp.size.accept(this);
            String javaType = getJavaType(exp.type);
            return "new " + javaType + "[" + size + "]";
        }
        // Lógica para 'data' (structs) virá no futuro
        return "new " + exp.type.baseType + "()";
    }

    // --- NOVO: Implementação do acesso a índice de array ---
    @Override
    public String visitExpIndex(ExpIndex exp) {
        String target = exp.target.accept(this);
        String index = exp.index.accept(this);
        return target + "[" + index + "]";
    }

    // --- NOVO: Implementação para quando um índice é um LValue (lado esquerdo da atribuição) ---
    @Override
    public String visitLValueIndex(LValueIndex lValueIndex) {
        String target = lValueIndex.target.accept(this);
        String index = lValueIndex.index.accept(this);
        return target + "[" + index + "]";
    }


    // --- ATUALIZADO: visitCmdAssign para lidar com LValueField ---
    @Override
    public String visitCmdAssign(CmdAssign cmd) {
        String expression = cmd.expression.accept(this);
        String target = cmd.target.accept(this); 

        if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            if (!declaredVariables.contains(varName)) {
                declaredVariables.add(varName);
                Type inferredType = inferExpressionType(cmd.expression);
                variableTypes.put(varName, inferredType);
                String javaType = getJavaType(inferredType);
                return javaType + " " + target + " = " + expression + ";\n";
            }
        }
        
        // Para LValueId (já declarado), LValueIndex e LValueField, a lógica é a mesma.
        return target + " = " + expression + ";\n";
    }

    @Override
    public String visitCmdIterate(CmdIterate cmd) {
        StringBuilder sb = new StringBuilder();
        
        if (cmd.condition instanceof ItCondExpr) {
            String loopVar = "_i" + loopCounter++;
            String limitExpr = cmd.condition.accept(this);
            sb.append("for (int ").append(loopVar).append(" = 0; ")
              .append(loopVar).append(" < ").append(limitExpr).append("; ")
              .append(loopVar).append("++) {\n");
            sb.append(cmd.body.accept(this));
            sb.append(indent()).append("}\n");
            
        } else if (cmd.condition instanceof ItCondLabelled) {
            ItCondLabelled cond = (ItCondLabelled) cmd.condition;
            String loopVar = cond.label;
            String iterableExpr = cond.expression.accept(this);
            
            // Inferencia de tipo para saber se é um array ou um inteiro
            Type iterableType = inferExpressionType(cond.expression);
            
            // --- NOVO: Lógica para iterar sobre ARRAYS ---
            if (iterableType.arrayDim > 0) {
                // Gera um laço for-each
                Type elementType = new Type(iterableType.baseType, iterableType.arrayDim - 1);
                String javaElementType = getJavaType(elementType);

                declaredVariables.add(loopVar);
                variableTypes.put(loopVar, elementType);
                
                sb.append("for (").append(javaElementType).append(" ").append(loopVar).append(" : ").append(iterableExpr).append(") {\n");
                sb.append(cmd.body.accept(this));
                sb.append(indent()).append("}\n");
                
                declaredVariables.remove(loopVar);
                variableTypes.remove(loopVar);

            } else { // Lógica existente para iterar com inteiros
                declaredVariables.add(loopVar);
                variableTypes.put(loopVar, new Type("Int", 0)); 
                sb.append("for (int ").append(loopVar).append(" = 0; ")
                  .append(loopVar).append(" < ").append(iterableExpr).append("; ")
                  .append(loopVar).append("++) {\n");
                sb.append(cmd.body.accept(this));
                sb.append(indent()).append("}\n");
                declaredVariables.remove(loopVar);
                variableTypes.remove(loopVar);
            }
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
    
    // --- ATUALIZADO: visitProg para lidar com 'data' ---
    @Override
    public String visitProg(Prog prog) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.util.Scanner;\n\n");
        sb.append("public class ").append(className).append(" {\n");
        
        // Limpa e pré-processa todas as definições
        allFunctions.clear();
        allDataTypes.clear();
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                allFunctions.put(((Fun) def).id, (Fun) def);
            } else if (def instanceof Data) {
                allDataTypes.put(((Data) def).name, (Data) def);
            }
        }

        indentLevel++;

        // 1. Gera as classes para os tipos 'data'
        for (Data data : allDataTypes.values()) {
            sb.append(data.accept(this));
        }

        // 2. Gera as classes de retorno para múltiplos retornos
        for (Fun fun : allFunctions.values()) {
            if (fun.retTypes.size() > 1) {
                sb.append(generateReturnClass(fun));
            }
        }
        
        // 3. Gera os métodos para as funções
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                sb.append(def.accept(this));
            }
        }

        indentLevel--;
        sb.append("}\n");
        return sb.toString();
    }

    // --- NOVO: Método para gerar a classe de retorno aninhada ---
    private String generateReturnClass(Fun fun) {
        StringBuilder sb = new StringBuilder();
        String returnClassName = fun.id + "_return";
        sb.append(indent()).append("public static class ").append(returnClassName).append(" {\n");
        indentLevel++;
        
        // Campos
        for (int i = 0; i < fun.retTypes.size(); i++) {
            String fieldType = getJavaType(fun.retTypes.get(i));
            sb.append(indent()).append("public ").append(fieldType).append(" ret").append(i).append(";\n");
        }

        // Construtor
        sb.append(indent()).append("public ").append(returnClassName).append("(");
        for (int i = 0; i < fun.retTypes.size(); i++) {
            String fieldType = getJavaType(fun.retTypes.get(i));
            sb.append(fieldType).append(" ret").append(i);
            if (i < fun.retTypes.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");
        indentLevel++;
        for (int i = 0; i < fun.retTypes.size(); i++) {
            sb.append(indent()).append("this.ret").append(i).append(" = ret").append(i).append(";\n");
        }
        indentLevel--;
        sb.append(indent()).append("}\n");

        indentLevel--;
        sb.append(indent()).append("}\n\n");
        return sb.toString();
    }

    // --- visitFun CORRIGIDO ---
    @Override
    public String visitFun(Fun fun) {
        this.currentFunction = fun;
        
        StringBuilder sb = new StringBuilder();
        declaredVariables.clear();
        variableTypes.clear();
        
        String returnType;
        if (fun.retTypes.isEmpty()) {
            returnType = "void";
        } else if (fun.retTypes.size() == 1) {
            returnType = getJavaType(fun.retTypes.get(0));
        } else {
            returnType = fun.id + "_return";
        }

        // CORREÇÃO APLICADA AQUI
        String params;
        if (fun.id.equals("main")) {
            params = "String[] args"; // Força a assinatura correta para o main
        } else {
            params = fun.params.stream()
                .map(p -> {
                    String paramType = getJavaType(p.type);
                    declaredVariables.add(p.id);
                    variableTypes.put(p.id, p.type);
                    return paramType + " " + p.id;
                })
                .collect(Collectors.joining(", "));
        }

        sb.append(indent()).append("public static ").append(returnType).append(" ").append(fun.id)
          .append("(").append(params).append(") {\n");
        
        if (fun.id.equals("main")) {
            indentLevel++;
            sb.append(indent()).append("Scanner _scanner = new Scanner(System.in);\n");
            indentLevel--;
        }

        sb.append(fun.body.accept(this));
        
        sb.append(indent()).append("}\n\n");
        this.currentFunction = null;
        return sb.toString();
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
    // --- visitCmdCall CORRIGIDO ---
    @Override
    public String visitCmdCall(CmdCall cmd) {
        String args = cmd.args.stream()
            .map(arg -> arg.accept(this))
            .collect(Collectors.joining(", "));

        String callExpr = cmd.id + "(" + args + ")";
        
        Fun calledFun = allFunctions.get(cmd.id);
        if (calledFun == null) {
            throw new IllegalStateException("Função '" + cmd.id + "' não encontrada.");
        }

        if (cmd.rets.isEmpty()) {
            return callExpr + ";\n";
        }
        
        // Múltiplos retornos
        if (calledFun.retTypes.size() > 1) {
            String tempVar = "_" + cmd.id + "_ret" + loopCounter++;
            String returnClassName = cmd.id + "_return";
            
            StringBuilder sb = new StringBuilder();
            sb.append(returnClassName).append(" ").append(tempVar).append(" = ").append(callExpr).append(";\n");

            for (int i = 0; i < cmd.rets.size(); i++) {
                LValue retLVal = cmd.rets.get(i);
                if (retLVal instanceof LValueId) {
                    String varName = ((LValueId) retLVal).id;
                    String assignmentSource = tempVar + ".ret" + i;
                    
                    // Verifica se a variável já foi declarada
                    if (!declaredVariables.contains(varName)) {
                        declaredVariables.add(varName);
                        Type returnType = calledFun.retTypes.get(i);
                        variableTypes.put(varName, returnType);
                        String javaType = getJavaType(returnType);
                        
                        // Gera a declaração completa
                        sb.append(indent()).append(javaType).append(" ").append(varName).append(" = ").append(assignmentSource).append(";\n");
                    } else {
                        // Apenas atribui
                        sb.append(indent()).append(varName).append(" = ").append(assignmentSource).append(";\n");
                    }
                } else {
                    // Lida com outros LValues (como v[i])
                    String target = retLVal.accept(this);
                    sb.append(indent()).append(target).append(" = ").append(tempVar).append(".ret").append(i).append(";\n");
                }
            }
            return sb.toString();
        } else { // Retorno único
            String target = cmd.rets.get(0).accept(this);
            if (cmd.rets.get(0) instanceof LValueId) {
                String varName = ((LValueId) cmd.rets.get(0)).id;
                if (!declaredVariables.contains(varName)) {
                     declaredVariables.add(varName);
                     Type returnType = calledFun.retTypes.get(0);
                     variableTypes.put(varName, returnType);
                     String javaType = getJavaType(returnType);
                     return javaType + " " + target + " = " + callExpr + ";\n";
                }
            }
            return target + " = " + callExpr + ";\n";
        }
    }
    // ATUALIZADO: para lidar com múltiplos valores de retorno
    @Override
    public String visitCmdReturn(CmdReturn cmd) {
        if (cmd.values.isEmpty()) {
            return "return;\n";
        }

        String values = cmd.values.stream()
            .map(v -> v.accept(this))
            .collect(Collectors.joining(", "));

        // Se a função atual tiver múltiplos retornos, instancie a classe de retorno
        if (currentFunction != null && currentFunction.retTypes.size() > 1) {
            return "return new " + currentFunction.id + "_return(" + values + ");\n";
        }

        // Caso contrário, é um retorno simples
        return "return " + values + ";\n";
    }
    @Override
    public String visitData(Data data) { return null; }
    @Override
    public String visitDataAbstract(DataAbstract data) { return null; }
    // --- NOVO: Visitor para DataRegular ---
    @Override
    public String visitDataRegular(DataRegular data) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("public static class ").append(data.name).append(" {\n");
        indentLevel++;
        for (Decl decl : data.declarations) {
            sb.append(indent()).append("public ").append(getJavaType(decl.type)).append(" ").append(decl.id).append(";\n");
        }
        indentLevel--;
        sb.append(indent()).append("}\n\n");
        return sb.toString();
    }
    @Override
    public String visitDecl(Decl decl) { return null; }
    @Override
    public String visitDef(Def def) { return null; }
    @Override
    public String visitExp(Exp exp) { return null; }
    // --- ATUALIZADO: Implementação de Chamadas de Função em Expressões ---
    @Override
    public String visitExpCall(ExpCall exp) {
        // Este método agora apenas gera a chamada base, ex: "soma(10, 5)"
        String args = exp.args.stream()
            .map(arg -> arg.accept(this))
            .collect(Collectors.joining(", "));
            
        return exp.id + "(" + args + ")";
    }
     @Override
    public String visitExpCallIndexed(ExpCallIndexed exp) {
        Fun calledFun = allFunctions.get(exp.call.id);
        if (calledFun == null) {
            throw new IllegalStateException("Função '" + exp.call.id + "' não encontrada.");
        }

        // Gera a chamada base, ex: "fibonacci(n - 1)"
        String functionCall = exp.call.accept(this);
        
        // Se a função tem um único retorno, o [0] é implícito e não gera código extra em Java.
        if (calledFun.retTypes.size() == 1) {
            return functionCall;
        }

        // Se a função tem múltiplos retornos, acessa o campo .retX do objeto de retorno.
        String index = exp.index.accept(this);
        return "(" + functionCall + ".ret" + index + ")";
    }
    // --- NOVO: Visitor para acesso a campos (p.x) ---
    @Override
    public String visitExpField(ExpField exp) {
        return exp.target.accept(this) + "." + exp.field;
    }
    @Override
    public String visitExpNull(ExpNull exp) { return null; }
    @Override
    public String visitItCond(ItCond itCond) { return null; }
    @Override
    public String visitLValue(LValue lValue) { return null; }
    // --- NOVO: Visitor para campos no lado esquerdo (p.x = ...) ---
    @Override
    public String visitLValueField(LValueField lValueField) {
        return lValueField.target.accept(this) + "." + lValueField.field;
    }
    // --- LValue VISITORS CORRIGIDOS ---
    // Esta é a correção principal. Agora o visitor sabe o que fazer
    // quando encontra uma variável simples no lado esquerdo de uma atribuição.
    @Override
    public String visitLValueId(LValueId lValueId) {
        return lValueId.id;
    }
    @Override
    public String visitParam(Param param) { return null; }
    @Override
    public String visitType(Type type) { return null; }
}