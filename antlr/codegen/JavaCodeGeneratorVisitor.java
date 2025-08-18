/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package codegen;

import ast.*;
import interpreter.Visitor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.HashMap;

/**
 * Implementa o Visitor para a geração de código source-to-source.
 * Percorre a Árvore Sintática Abstrata (AST) da linguagem Lang e a traduz
 * para um código-fonte Java funcional e semanticamente equivalente.
 */

public class JavaCodeGeneratorVisitor implements Visitor<String> {

    private final String className; // Nome da classe Java a ser gerada.

    // Conjunto para rastrear variáveis já declaradas no escopo atual.
    private final Set<String> declaredVariables = new HashSet<>();

    // Mapa para associar nomes de variáveis aos seus tipos (Type).
    private final Map<String, Type> variableTypes = new java.util.HashMap<>();


    // Rastreia a definição da função que está sendo visitada atualmente.
    private Fun currentFunction = null; 

    // Tabela de símbolos global para todas as funções do programa.
    private Map<String, Fun> allFunctions = new java.util.HashMap<>();
    
     // Tabela de símbolos global para todos os tipos 'data' definidos.
    private Map<String, Data> allDataTypes = new java.util.HashMap<>();
    
    // Flag para indicar se o visitor está dentro de um método de 'abstract data'.
    private boolean isInsideDataMethod = false;
    
    // Mapa para gerenciar o renomeamento de variáveis sombreadas (ex: em laços).
    private Map<String, String> remappedVariables = new HashMap<>();
    
    // Contador para o nível de indentação do código gerado.
    private int indentLevel = 0;

    // Contador para gerar nomes únicos para variáveis de laço internas.
    private int loopCounter = 0; 

    public JavaCodeGeneratorVisitor(String className) {
        this.className = className;
    }

    // Método auxiliar que gera uma string de espaços para a indentação do código.
    private String indent() {
        return "    ".repeat(indentLevel);
    }

    // Infére o tipo de uma expressão Atua como uma análise semântica simplificada, usada quando a informação de tipo não foi pré-calculada pelo SemanticVisitor
    private Type inferExpressionType(Exp exp) {

        // Prioriza o tipo já anotado pelo analisador semântico, se existir.
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

    // Traduz uma expressão 'new', seja para um array ou para uma instância de 'data'
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

    // Traduz o acesso a um índice de array em uma expressão (ex: v[i])
    @Override
    public String visitExpIndex(ExpIndex exp) {
        String target = exp.target.accept(this);
        String index = exp.index.accept(this);
        return target + "[" + index + "]";
    }

    // Traduz um LValue de índice de array, usado no lado esquerdo de uma atribuição
    @Override
    public String visitLValueIndex(LValueIndex lValueIndex) {
        String target = lValueIndex.target.accept(this);
        String index = lValueIndex.index.accept(this);
        return target + "[" + index + "]";
    }

    // Traduz um comando de atribuição.
    // Gera uma declaração de variável na primeira atribuição, ou uma simples atribuição para variáveis já existentes, índices de array ou campos
    @Override
    public String visitCmdAssign(CmdAssign cmd) {
        String expression = cmd.expression.accept(this);
        String target = cmd.target.accept(this); 

        // Se for uma atribuição a uma variável simples pela primeira vez...
        if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            if (!declaredVariables.contains(varName)) {
                // ...registra e gera uma declaração com tipo.
                declaredVariables.add(varName);
                Type inferredType = inferExpressionType(cmd.expression);
                variableTypes.put(varName, inferredType);
                String javaType = getJavaType(inferredType);
                return javaType + " " + target + " = " + expression + ";\n";
            }
        }
        
        // Para variáveis já declaradas e outros LValues (v[i], p.x), gera uma atribuição simples
        return target + " = " + expression + ";\n";
    }

    // Traduz um comando 'iterate', lidando com as duas variantes (com e sem rótulo) e com o sombreamento de variáveis
    @Override
    public String visitCmdIterate(CmdIterate cmd) {
        StringBuilder sb = new StringBuilder();
        if (cmd.condition instanceof ItCondExpr) {
            // Caso: iterate(expr) -> for (int _i = 0; _i < expr; _i++)
            String loopVar = "_i" + loopCounter++;
            String limitExpr = cmd.condition.accept(this);
            sb.append("for (int ").append(loopVar).append(" = 0; ")
              .append(loopVar).append(" < ").append(limitExpr).append("; ")
              .append(loopVar).append("++) {\n");
            sb.append(cmd.body.accept(this));
            sb.append(indent()).append("}\n");
        } else if (cmd.condition instanceof ItCondLabelled) {
            ItCondLabelled cond = (ItCondLabelled) cmd.condition;
            String originalLoopVar = cond.label;
            String iterableExpr = cond.expression.accept(this);
            Type iterableType = inferExpressionType(cond.expression);

            boolean isShadowing = declaredVariables.contains(originalLoopVar);
            String actualLoopVar = originalLoopVar;

            // Se a variável do laço já existe no escopo, renomeia para evitar conflito
            if (isShadowing) {
                actualLoopVar = originalLoopVar + "_" + loopCounter++;
                remappedVariables.put(originalLoopVar, actualLoopVar);
            }

            // Salva o estado da variável para restaurar depois
            Type shadowedType = variableTypes.get(originalLoopVar);

            if (iterableType.arrayDim > 0) {
                // Caso: iterate(elem : array) -> for (Type elem : array)
                Type elementType = new Type(iterableType.baseType, iterableType.arrayDim - 1);
                String javaElementType = getJavaType(elementType);
                
                declaredVariables.add(originalLoopVar);
                variableTypes.put(originalLoopVar, elementType);
                
                sb.append("for (").append(javaElementType).append(" ").append(actualLoopVar).append(" : ").append(iterableExpr).append(") {\n");
                sb.append(cmd.body.accept(this));
                sb.append(indent()).append("}\n");

            } else { // Caso: iterate(i : int_expr) -> for (int i = 0; ...)
                declaredVariables.add(originalLoopVar);
                variableTypes.put(originalLoopVar, new Type("Int", 0));

                sb.append("for (int ").append(actualLoopVar).append(" = 0; ")
                  .append(actualLoopVar).append(" < ").append(iterableExpr).append("; ")
                  .append(actualLoopVar).append("++) {\n");
                sb.append(cmd.body.accept(this));
                sb.append(indent()).append("}\n");
            }
            
            // Restaura o estado da variável sombreada após o laço
            if (isShadowing) {
                remappedVariables.remove(originalLoopVar);
                variableTypes.put(originalLoopVar, shadowedType);
            } else {
                declaredVariables.remove(originalLoopVar);
                variableTypes.remove(originalLoopVar);
            }
        }
        return sb.toString();
    }

    // Traduz a parte da expressão de uma condição de 'iterate' com rótulo (ex: iterate(i: 5)). Apenas visita a expressão para obter sua tradução em Java
    @Override
    public String visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return itCondLabelled.expression.accept(this);
    }

    // Traduz a parte da expressão de uma condição de 'iterate' sem rótulo (ex: iterate(5))
    @Override
    public String visitItCondExpr(ItCondExpr itCondExpr) {
        // Apenas visita a expressão interna
        return itCondExpr.expression.accept(this);
    }

    // Traduz um LValue que é um identificador (ex: a variável 'x'). 
    // Verifica se a variável foi renomeada devido ao sombreamento em um laço e retorna o nome correto a ser usado no código Java gerado
    @Override
    public String visitLValueId(LValueId lValueId) {
        return remappedVariables.getOrDefault(lValueId.id, lValueId.id);
    }
    
    /**
     * Ponto de entrada da geração de código para um programa inteiro.
     * Orquestra a tradução em uma ordem específica:
     * 1. Pré-processa todas as definições de 'data' e 'fun'.
     * 2. Gera as classes Java para os tipos 'data'.
     * 3. Gera as classes auxiliares para funções com múltiplos retornos.
     * 4. Gera os métodos Java para as funções 'lang'.
     */
    @Override
    public String visitProg(Prog prog) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.util.Scanner;\n\n");
        sb.append("public class ").append(className).append(" {\n");
        
        // Fase de pré-processamento: cataloga todas as definições para referência futura
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

    // Método auxiliar que gera uma classe estática aninhada para simular múltiplos retornos em Java
    private String generateReturnClass(Fun fun) {
        StringBuilder sb = new StringBuilder();
        String returnClassName = fun.id + "_return";
        sb.append(indent()).append("public static class ").append(returnClassName).append(" {\n");
        indentLevel++;
        
        // Gera os campos públicos (ret0, ret1, ...)
        for (int i = 0; i < fun.retTypes.size(); i++) {
            String fieldType = getJavaType(fun.retTypes.get(i));
            sb.append(indent()).append("public ").append(fieldType).append(" ret").append(i).append(";\n");
        }

        // Gera o construtor para inicializar todos os campos
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

    /**
     * Traduz uma definição de função 'lang' para um método Java.
     * Lida com a assinatura do método (modificadores, tipo de retorno, nome, parâmetros)
     * e delega a tradução do corpo para 'visitCmdBlock'.
     */
    @Override
    public String visitFun(Fun fun) {
        this.currentFunction = fun;
        
        StringBuilder sb = new StringBuilder();
        // Limpa os escopos para cada nova função
        declaredVariables.clear();
        variableTypes.clear();
        
        // Determina o tipo de retorno do método Java
        String returnType;
        if (fun.retTypes.isEmpty()) {
            returnType = "void";
        } else if (fun.retTypes.size() == 1) {
            returnType = getJavaType(fun.retTypes.get(0));
        } else {
            returnType = fun.id + "_return";
        }

        // Traduz a lista de parâmetros
        String params;
        if (fun.id.equals("main")) {
            params = "String[] args"; // Força a assinatura padrão do 'main' em Java
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

        // Monta a assinatura completa do método
        sb.append(indent()).append("public static ").append(returnType).append(" ").append(fun.id)
          .append("(").append(params).append(") {\n");
        
        // Adiciona a criação do Scanner apenas dentro do 'main'
        if (fun.id.equals("main")) {
            indentLevel++;
            sb.append(indent()).append("Scanner _scanner = new Scanner(System.in);\n");
            indentLevel--;
        }

        // Traduz o corpo da função
        sb.append(fun.body.accept(this));
        
        sb.append(indent()).append("}\n\n");
        this.currentFunction = null;
        return sb.toString();
    }

      
    // Traduz o comando 'read' para código Java
    @Override
    public String visitCmdRead(CmdRead cmd) {
        StringBuilder sb = new StringBuilder();
        
        // Gera o código para o alvo da leitura (pode ser 'x', 'v[i]' ou 'p.x').
        String target = cmd.lvalue.accept(this);
        
        // Infére o tipo do alvo para saber qual método do Scanner usar.
        Type targetType = inferExpressionType(cmd.lvalue);
        if (targetType == null) {
            throw new IllegalStateException("Não foi possível inferir o tipo para o alvo do comando 'read'.");
        }
        
        sb.append("System.out.print(\" entrada> \");\n");
        sb.append(indent());

        // Escolhe o método do Scanner com base no tipo inferido.
        switch (targetType.baseType) {
            case "Int":
                sb.append(target).append(" = _scanner.nextInt();\n");
                break;
            case "Float":
                sb.append(target).append(" = _scanner.nextFloat();\n");
                break;
            default: // Para Char, String, etc.
                sb.append(target).append(" = _scanner.next();\n");
                break;
        }
        return sb.toString();
    }
    
    // Traduz um bloco de comandos. Aumenta o nível de indentação, visita cada comando filho e depois diminui o nível de indentação
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
    
    // Traduz um comando 'print'
    @Override
    public String visitCmdPrint(CmdPrint cmd) {
        String value = cmd.value.accept(this);
        // Tratamento especial: se for um caractere de nova linha, usa println(), senão usa print().
        if (cmd.value instanceof ExpChar && ((ExpChar) cmd.value).value == '\n') {
            return "System.out.println();\n";
        }
        return "System.out.print(" + value + ");\n";
    }
    
    // Traduz um comando 'if-then-else'
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

    // Método auxiliar para mapear um tipo da linguagem 'lang' para seu equivalente em Java.
    private String getJavaType(Type langType) {
        if (langType == null) {
            throw new IllegalStateException("Erro: Tipo da expressão é nulo.");
        }

        // Lida com tipos de array recursivamente
        if (langType.arrayDim > 0) {
            return getJavaType(new Type(langType.baseType, 0)) + "[]".repeat(langType.arrayDim);
        }

        // Mapeia os tipos base.
        switch (langType.baseType) {
            case "Int":
                return "int";
            case "Float":
                return "float";
            case "Bool":
                return "boolean";
            case "Char":
                return "String"; // Char de 'lang' é traduzido para String em Java para simplicidade
            default:
                return langType.baseType; // Para tipos 'data' definidos pelo usuário
        }
    }
    
    // Traduz um literal inteiro
    @Override
    public String visitExpInt(ExpInt exp) {
        return String.valueOf(exp.value);
    }
    
     // Traduz um literal de ponto flutuante
    @Override
    public String visitExpFloat(ExpFloat exp) {
        // Adiciona o sufixo 'f' para que o Java trate o literal como float, não double
        return String.valueOf(exp.value) + "f";
    }

    // Traduz um literal de caractere, tratando caracteres de escape
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
            default:
                // Para caracteres normais, apenas os coloca entre aspas duplas
                return "\"" + c + "\"";
        }
    }
    
    // Traduz um literal booleano
    @Override
    public String visitExpBool(ExpBool exp) {
        return String.valueOf(exp.value);
    }

    // Traduz uma expressão binária
    @Override
    public String visitExpBinOp(ExpBinOp exp) {
        String left = exp.left.accept(this);
        String right = exp.right.accept(this);
        return "(" + left + " " + exp.op + " " + right + ")";
    }

    // Traduz uma expressão entre parênteses
    @Override
    public String visitExpParen(ExpParen exp) {
        return "(" + exp.exp.accept(this) + ")";
    }

    // Traduz uma expressão unária
    @Override
    public String visitExpUnaryOp(ExpUnaryOp exp) {
        String operand = exp.exp.accept(this);
        // Garante os parênteses para a precedência correta
        return "(" + exp.op + operand + ")";
    }

    @Override
    public String visitCmd(Cmd exp) { return null; }

    // Traduz uma chamada de função usada como um comando (procedimento). Lida com atribuição de retorno único e múltiplo.
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

         // Caso de chamada sem atribuição de retorno
        if (cmd.rets.isEmpty()) {
            return callExpr + ";\n";
        }
        
        // Caso de múltiplos retornos
        if (calledFun.retTypes.size() > 1) {
            String tempVar = "_" + cmd.id + "_ret" + loopCounter++;
            String returnClassName = cmd.id + "_return";
            
            StringBuilder sb = new StringBuilder();
            // 1. Armazena o objeto de retorno em uma variável temporária
            sb.append(returnClassName).append(" ").append(tempVar).append(" = ").append(callExpr).append(";\n");

            // 2. Desempacota os valores para as variáveis de destino
            for (int i = 0; i < cmd.rets.size(); i++) {
                LValue retLVal = cmd.rets.get(i);
                if (retLVal instanceof LValueId) {
                    String varName = ((LValueId) retLVal).id;
                    String assignmentSource = tempVar + ".ret" + i;
                    
                    // Se a variável de destino não foi declarada ainda, gera a declaração
                    if (!declaredVariables.contains(varName)) {
                        declaredVariables.add(varName);
                        Type returnType = calledFun.retTypes.get(i);
                        variableTypes.put(varName, returnType);
                        String javaType = getJavaType(returnType);
                        
                        // Gera a declaração completa
                        sb.append(indent()).append(javaType).append(" ").append(varName).append(" = ").append(assignmentSource).append(";\n");
                    } else { // Se já existe, apenas atribui
                        // Apenas atribui
                        sb.append(indent()).append(varName).append(" = ").append(assignmentSource).append(";\n");
                    }
                } else { // Para atribuição a v[i] ou p.x
                    String target = retLVal.accept(this);
                    sb.append(indent()).append(target).append(" = ").append(tempVar).append(".ret").append(i).append(";\n");
                }
            }
            return sb.toString();
        } else { // Caso de retorno único
            String target = cmd.rets.get(0).accept(this);
            if (cmd.rets.get(0) instanceof LValueId) {
                String varName = ((LValueId) cmd.rets.get(0)).id;
                // Se a variável não foi declarada, gera a declaração
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

    // Traduz um comando 'return'. Lida com os casos de retorno vazio, retorno único e múltiplos retornos
    @Override
    public String visitCmdReturn(CmdReturn cmd) {
        // Caso de 'return' sem valor (em uma função void)
        if (cmd.values.isEmpty()) {
            return "return;\n";
        }

        // Traduz todas as expressões de retorno e as une com vírgulas
        String values = cmd.values.stream()
            .map(v -> v.accept(this))
            .collect(Collectors.joining(", "));

        // Se a função atual tem múltiplos retornos, gera a instanciação da classe de retorno
        if (currentFunction != null && currentFunction.retTypes.size() > 1) {
            return "return new " + currentFunction.id + "_return(" + values + ");\n";
        }

        // Caso contrário, é um retorno de valor simples
        return "return " + values + ";\n";
    }

    // Método para o nó abstrato 'Data'. Não é chamado diretamente
    @Override
    public String visitData(Data data) { return null; }

    // Traduz a definição de um 'abstract data' para uma classe Java estática aninhada. Gera os campos como 'private' e os métodos internos como 'public'
    @Override
    public String visitDataAbstract(DataAbstract data) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("public static class ").append(data.name).append(" {\n");
        indentLevel++;

        // Gera campos como 'private' para garantir o encapsulamento
        for (Decl decl : data.declarations) {
            sb.append(indent()).append("private ").append(getJavaType(decl.type)).append(" ").append(decl.id).append(";\n");
        }
        sb.append("\n");

        // Gera os métodos públicos da classe
        isInsideDataMethod = true;
        for (Fun fun : data.functions) {
            sb.append(fun.accept(this));
        }
        isInsideDataMethod = false;

        indentLevel--;
        sb.append(indent()).append("}\n\n");
        return sb.toString();
    }

    // Traduz a definição de um 'data' regular para uma classe Java estática aninhada. Os campos são gerados como 'public' por padrão
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

    // Método para um nó de declaração de campo (Decl). Não realiza nenhuma ação pois os campos são traduzidos dentro de 'visitData...'
    @Override
    public String visitDecl(Decl decl) { return null; }

    // Método para o nó abstrato 'Def'. Não é chamado diretamente
    @Override
    public String visitDef(Def def) { return null; }
    
    // Método para o nó abstrato 'Exp'. Não é chamado diretamente
    @Override
    public String visitExp(Exp exp) { return null; }

    // Traduz uma chamada de função que é usada dentro de uma expressão
    @Override
    public String visitExpCall(ExpCall exp) {
        // Apenas traduz a chamada base, ex: "soma(10, 5)".
        // A lógica de acesso ao índice é tratada em 'visitExpCallIndexed'.
        String args = exp.args.stream()
            .map(arg -> arg.accept(this))
            .collect(Collectors.joining(", "));
            
        return exp.id + "(" + args + ")";
    }

    // Traduz uma chamada de função com acesso a um índice de retorno (ex: func()[0])
    @Override
    public String visitExpCallIndexed(ExpCallIndexed exp) {
        Fun calledFun = allFunctions.get(exp.call.id);
        if (calledFun == null) {
            throw new IllegalStateException("Função '" + exp.call.id + "' não encontrada.");
        }

        // Gera a chamada base, ex: "fibonacci(n - 1)"
        String functionCall = exp.call.accept(this);
        
        // Se a função tem um único retorno, o [0] é implícito e não gera código extra em Java
        if (calledFun.retTypes.size() == 1) {
            return functionCall;
        }

        // Se a função tem múltiplos retornos, acessa o campo .retX do objeto de retorno
        String index = exp.index.accept(this);
        return "(" + functionCall + ".ret" + index + ")";
    }

    // Traduz o acesso a um campo de 'data' em uma expressão (ex: p.x)
    @Override
    public String visitExpField(ExpField exp) {
        return exp.target.accept(this) + "." + exp.field;
    }

    // Traduz o literal 'null'
    @Override
    public String visitExpNull(ExpNull exp) {
        return "null";
    }

    // Método para o nó abstrato 'ItCond'. Não é chamado diretamente
    @Override
    public String visitItCond(ItCond itCond) { return null; }
    
    // Método para o nó abstrato 'LValue'. Não é chamado diretamente
    @Override
    public String visitLValue(LValue lValue) { return null; }
    
    // Traduz um LValue que é um campo de 'data' (ex: p.x = ...)
    @Override
    public String visitLValueField(LValueField lValueField) {
        return lValueField.target.accept(this) + "." + lValueField.field;
    }

    // Traduz uma variável em uma expressão, considerando se ela foi renomeada
    @Override
    public String visitExpVar(ExpVar exp) {
        return remappedVariables.getOrDefault(exp.name, exp.name);
    }
    
    // Método para o nó de Parâmetro de função. Não realiza nenhuma ação pois os parâmetros são traduzidos em 'visitFun'
    @Override
    public String visitParam(Param param) { return null; }
    
    // Método para o nó de Tipo. Não possui ação na geração de código
    @Override
    public String visitType(Type type) { return null; }
}