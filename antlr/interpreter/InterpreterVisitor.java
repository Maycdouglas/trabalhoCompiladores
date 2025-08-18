/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Implementa o Visitor para interpretar a AST da linguagem Lang.
 * Percorre a árvore de sintaxe abstrata e executa os comandos,
 * simulando o comportamento do programa.
 */
public class InterpreterVisitor implements Visitor<Object> {

    private final Memory memory = new Memory();
    private final Scanner scanner = new Scanner(System.in);

    public InterpreterVisitor() {
        memory.pushScope(); // Inicializa o escopo global
    }

    // Método base para nós de comando. Não deve ser chamado diretamente
    @Override
    public Object visitCmd(Cmd cmd) {
        throw new UnsupportedOperationException("visitCmd deve ser chamado apenas em subclasses concretas.");
    }

    // Interpreta um comando de atribuição (ex: x = 10, v[0] = 1, p.x = 2).
    // Avalia a expressão à direita e armazena o resultado no LValue à esquerda.
    @Override
    public Object visitCmdAssign(CmdAssign cmd) {
        Value valueToAssign = (Value) cmd.expression.accept(this);

        if (cmd.target instanceof LValueIndex) {
            LValueIndex lvalIndex = (LValueIndex) cmd.target;

             // Atribuição a um índice de array (ex: v[i] = ...)
            Value arrayVal = (Value) lvalIndex.target.accept(this);
            Value indexVal = (Value) lvalIndex.index.accept(this);

            if (arrayVal instanceof ArrayValue && indexVal instanceof IntValue) {
                int index = ((IntValue) indexVal).getValue();
                ((ArrayValue) arrayVal).set(index, valueToAssign);
            } else {
                throw new RuntimeException(
                        "Atribuição inválida em array. O alvo não é um array ou o índice não é um inteiro.");
            }

        } else if (cmd.target instanceof LValueField) {

            LValueField lvalField = (LValueField) cmd.target;
            // Atribuição a um campo de 'data' (ex: p.x = ...)
            Value objVal = (Value) lvalField.target.accept(this);

            if (!(objVal instanceof DataValue)) {
                throw new RuntimeException("Tentativa de atribuir campo em algo que não é um 'data'.");
            }

            ((DataValue) objVal).setField(lvalField.field, valueToAssign);

        } else if (cmd.target instanceof LValueId) {
            // Atribuição a uma variável simples (ex: i = 0)
            String varName = ((LValueId) cmd.target).id;
            memory.currentScope().put(varName, valueToAssign);

        } else {
            throw new UnsupportedOperationException("Tipo de atribuição não suportado.");
        }

        return null; // Comandos não retornam valor
    }

    // Interpreta um bloco de comandos, executando cada comando em sequência
    @Override
    public Object visitCmdBlock(CmdBlock cmd) {
        for (Cmd commandInBlock : cmd.cmds) {
            commandInBlock.accept(this);
        }
        return null;
    }

    // Interpreta uma chamada de função como um comando
    @Override
    public Object visitCmdCall(CmdCall cmd) {
        Fun funDef = memory.getFunction(cmd.id);
        if (funDef == null) {
            throw new RuntimeException("Função '" + cmd.id + "' não definida.");
        }
        if (funDef.params.size() != cmd.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + cmd.id + "'.");
        }

        // 1. Avalia os argumentos e prepara o novo escopo para a função
        Map<String, Value> newScope = new HashMap<>();
        for (int i = 0; i < funDef.params.size(); i++) {
            String paramName = funDef.params.get(i).id;
            Value argValue = (Value) cmd.args.get(i).accept(this);
            newScope.put(paramName, argValue);
        }

        // 2. Empilha o novo escopo e executa o corpo da função
        memory.pushScope(newScope);
        List<Value> returnedValues = new ArrayList<>(); // Inicializa a lista como vazia
        try {
            funDef.body.accept(this);
        } catch (ReturnException re) {
            // Captura a exceção de retorno para obter os valores
            returnedValues = re.getValues();
        } finally {
            // Garante que o escopo seja removido, mesmo que não haja return
            memory.popScope();
        }

        // 3. Atribui os valores retornados às variáveis de retorno, se houver
        if (cmd.rets != null && !cmd.rets.isEmpty()) {
            if (returnedValues.size() != cmd.rets.size()) {
                throw new RuntimeException("Número de variáveis de retorno (" + cmd.rets.size()
                        + ") é diferente do número de valores retornados (" + returnedValues.size() + ") pela função '"
                        + cmd.id + "'.");
            }
            for (int i = 0; i < cmd.rets.size(); i++) {
                LValue targetLval = cmd.rets.get(i);
                Value returnedValue = returnedValues.get(i);
                if (targetLval instanceof LValueId) {
                    memory.currentScope().put(((LValueId) targetLval).id, returnedValue);
                } else {
                    throw new UnsupportedOperationException(
                            "Atribuição de retorno múltiplo só suporta variáveis simples.");
                }
            }
        }

        return null;
    }


    
    // Interpreta um comando 'if'. Avalia a condição e executa o bloco 'then' ou 'else'
    @Override
    public Object visitCmdIf(CmdIf cmd) {
        Value condition = (Value) cmd.condition.accept(this);
        if (condition instanceof BoolValue && ((BoolValue) condition).getValue()) {
            cmd.thenBranch.accept(this);
        } else if (cmd.elseBranch != null) {
            cmd.elseBranch.accept(this);
        }
        return null;
    }

    // Interpreta um comando 'iterate'
    @Override
    public Object visitCmdIterate(CmdIterate cmd) {
        if (cmd.condition instanceof ItCondLabelled itCond) {
            // Laço com rótulo (ex: iterate(i: v) ou iterate(i: 5))
            Value iterable = (Value) itCond.expression.accept(this);

            if (iterable instanceof ArrayValue array) {
                // Itera sobre os elementos de um array
                for (Value element : array.getValues()) {
                    memory.currentScope().put(itCond.label, element);
                    cmd.body.accept(this);
                }
            } else if (iterable instanceof IntValue intVal) {
                // Itera de 0 até o valor do inteiro
                int max = intVal.getValue();
                for (int i = 0; i < max; i++) {
                    memory.currentScope().put(itCond.label, new IntValue(i));
                    cmd.body.accept(this);
                }
            } else {
                throw new UnsupportedOperationException("'iterate' com rótulo só suporta arrays ou inteiros.");
            }
        } else if (cmd.condition instanceof ItCondExpr itCondExpr) {
            // Laço sem rótulo (ex: iterate(n))
            Value iterable = (Value) itCondExpr.expression.accept(this);

            if (iterable instanceof IntValue intVal) {
                int max = intVal.getValue();
                for (int i = 0; i < max; i++) {
                    cmd.body.accept(this);
                }
            } else {
                throw new UnsupportedOperationException("'iterate' sem rótulo só suporta inteiros.");
            }
        } else {
            throw new UnsupportedOperationException("Tipo de condição de iterate não suportado.");
        }
        return null;
    }

    // Interpreta um comando 'print'. Avalia a expressão e imprime seu valor na saída padrão
    @Override
    public Object visitCmdPrint(CmdPrint cmd) {
        Value valueToPrint = (Value) cmd.value.accept(this);
        System.out.print(valueToPrint.toString());
        return null;
    }

    // Interpreta um comando 'read'. Lê um valor da entrada padrão e o atribui à variável
    @Override
    public Object visitCmdRead(CmdRead cmd) {
        String varName = extractVarName(cmd.lvalue);
        System.out.print(" entrada> ");

        // Tenta ler como Int, depois como Float. Se falhar, avisa o usuário
        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            memory.currentScope().put(varName, new IntValue(value));
        } else if (scanner.hasNextFloat()) {
            float value = scanner.nextFloat();
            memory.currentScope().put(varName, new FloatValue(value));
        } else {
            String input = scanner.next();
            System.err.println(
                    "Aviso: Entrada '" + input + "' não é um número. Variável '" + varName + "' não foi atualizada.");
        }
        return null;
    }

    // Interpreta um comando 'return'. Avalia as expressões e lança uma ReturnException para ser capturada pelo visitador da chamada de função
    @Override
    public Object visitCmdReturn(CmdReturn cmd) {
        List<Value> values = new ArrayList<>();
        for (Exp exp : cmd.values) {
            values.add((Value) exp.accept(this));
        }
        throw new ReturnException(values);
    }


    // Processa uma definição de 'data', delegando para o método específico (visitDataAbstract ou visitDataRegular)
    @Override
    public Object visitData(Data data) {
        if (data instanceof DataAbstract) {
            return visitDataAbstract((DataAbstract) data);
        } else if (data instanceof DataRegular) {
            return visitDataRegular((DataRegular) data);
        }
        throw new RuntimeException("Tipo de data desconhecido: " + data.getClass().getName());
    }

    // Registra a definição de um 'abstract data' e todas as suas funções internas na memória
    @Override
    public Object visitDataAbstract(DataAbstract data) {
        // Armazena a definição do tipo abstrato
        memory.setDataDef(data.name, data);
        // Também registrar funções internas internas para que possam ser chamadas posteriormente
        for (Fun fun : data.functions) {
            memory.setFunction(fun.id, fun);
        }
        return null;
    }

    // Registra a definição de um 'data' regular na memória
    @Override
    public Object visitDataRegular(DataRegular data) {
        // Armazena a definição do tipo regular
        memory.setDataDef(data.name, data);
        return null;
    }

    // Método para um nó de declaração de campo (Decl). 
    // Não realiza nenhuma ação durante a interpretação, pois os campos são tratados ao criar um DataValue.
    @Override
    public Object visitDecl(Decl decl) {
        return null;
    }
    
    // Método para o nó abstrato 'Def'. Não é chamado diretamente.
    @Override
    public Object visitDef(Def def) {
        return null;
    }

    // Método para o nó abstrato 'Exp'. Apenas delega a chamada para a implementação concreta do nó de expressão
    @Override
    public Object visitExp(Exp exp) {
        return exp.accept(this);
    }

    //  Avalia uma expressão binária (ex: a + b, c < d). Visita recursivamente os operandos esquerdo e direito, e então aplica o operador
    @Override
    public Object visitExpBinOp(ExpBinOp exp) {
        Value left = (Value) exp.left.accept(this);
        Value right = (Value) exp.right.accept(this);

        // Tratamento especial para comparações de igualdade com 'null'
        if (exp.op.equals("==")) {
            if (left instanceof NullValue && right instanceof NullValue) {
                return new BoolValue(true);
            }
            if (left instanceof NullValue || right instanceof NullValue) {
                return new BoolValue(false);
            }
        } else if (exp.op.equals("!=")) {
            if (left instanceof NullValue && right instanceof NullValue) {
                return new BoolValue(false);
            }
            if (left instanceof NullValue || right instanceof NullValue) {
                return new BoolValue(true);
            }
        }

        // Operações aritméticas e de comparação para inteiros
        if (left instanceof IntValue && right instanceof IntValue) {
            int l = ((IntValue) left).getValue();
            int r = ((IntValue) right).getValue();
            switch (exp.op) {
                case "+":
                    return new IntValue(l + r);
                case "-":
                    return new IntValue(l - r);
                case "*":
                    return new IntValue(l * r);
                case "/":
                    return new IntValue(l / r);
                case "%":
                    return new IntValue(l % r);
                case "==":
                    return new BoolValue(l == r);
                case "!=":
                    return new BoolValue(l != r);
                case "<":
                    return new BoolValue(l < r);
            }
        }

        // Operações aritméticas e de comparação para floats (com promoção de int para float)
        if ((left instanceof IntValue || left instanceof FloatValue)
                && (right instanceof IntValue || right instanceof FloatValue)) {
            float l = (left instanceof IntValue) ? ((IntValue) left).getValue() : ((FloatValue) left).getValue();
            float r = (right instanceof IntValue) ? ((IntValue) right).getValue() : ((FloatValue) right).getValue();
            switch (exp.op) {
                case "+":
                    return new FloatValue(l + r);
                case "-":
                    return new FloatValue(l - r);
                case "*":
                    return new FloatValue(l * r);
                case "/":
                    return new FloatValue(l / r);
                case "==":
                    return new BoolValue(l == r);
                case "!=":
                    return new BoolValue(l != r);
                case "<":
                    return new BoolValue(l < r);
            }
        }

        // Operações de comparação para caracteres
        if (left instanceof CharValue && right instanceof CharValue) {
            char l = ((CharValue) left).getValue();
            char r = ((CharValue) right).getValue();
            switch (exp.op) {
                case "==":
                    return new BoolValue(l == r);
                case "!=":
                    return new BoolValue(l != r);
                case "<":
                    return new BoolValue(l < r);
            }
        }

        // Operações lógicas para booleanos (com coerção de inteiro para booleano)
        if ((exp.op.equals("&&") || exp.op.equals("||")) &&
                (left instanceof IntValue || left instanceof BoolValue) &&
                (right instanceof IntValue || right instanceof BoolValue)) {

            boolean l = (left instanceof IntValue) ? ((IntValue) left).getValue() != 0
                    : ((BoolValue) left).getValue();

            boolean r = (right instanceof IntValue) ? ((IntValue) right).getValue() != 0
                    : ((BoolValue) right).getValue();

            return new BoolValue(exp.op.equals("&&") ? (l && r) : (l || r));
        }

        throw new RuntimeException("Operação binária não suportada: " + left.getClass().getSimpleName() + " " + exp.op
                + " " + right.getClass().getSimpleName());
    }

    // Converte um literal booleano da AST em um BoolValue
    @Override
    public Object visitExpBool(ExpBool exp) {
        return new BoolValue(exp.value);
    }
    
    // Avalia uma chamada de função que é usada dentro de uma expressão. Retorna uma ArrayValue contendo todos os valores retornados pela função
    @Override
    public Object visitExpCall(ExpCall exp) {
        Fun funDef = memory.getFunction(exp.id);
        if (funDef == null) {
            throw new RuntimeException("Função '" + exp.id + "' não definida.");
        }

        if (funDef.params.size() != exp.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + exp.id + "'.");
        }

        Map<String, Value> newScope = new HashMap<>();
        for (int i = 0; i < funDef.params.size(); i++) {
            String paramName = funDef.params.get(i).id;
            Value argValue = (Value) exp.args.get(i).accept(this);
            newScope.put(paramName, argValue);
        }

        memory.pushScope(newScope);

        List<Value> returned = null;
        try {
            funDef.body.accept(this);
        } catch (ReturnException re) {
            returned = re.getValues();
        } finally {
            memory.popScope();
        }

        if (returned == null) {
            throw new RuntimeException(
                    "A função '" + exp.id + "' foi usada em uma expressão mas não retornou um valor.");
        }

        // Encapsula todos os retornos em um ArrayValue para consistência com ExpCallIndexed
        ArrayValue arrayValue = new ArrayValue(returned.size());
        for (int i = 0; i < returned.size(); i++) {
            arrayValue.set(i, returned.get(i));
        }

        return arrayValue;
    }

    // Avalia uma chamada de função com acesso a um índice de retorno (ex: func()[0])
    @Override
    public Object visitExpCallIndexed(ExpCallIndexed exp) {
        // Primeiro, executa a chamada de função, que retorna um ArrayValue
        Object result = exp.call.accept(this);

        if (!(result instanceof ArrayValue arr)) {
            throw new RuntimeException("Function call did not return an array: " + result);
        }

        // Em seguida, avalia o índice
        Object indexObj = exp.index.accept(this);
        if (!(indexObj instanceof IntValue intVal)) {
            throw new RuntimeException("Index must evaluate to an integer.");
        }
        int index = intVal.getValue();

        // Retorna o valor no índice especificado
        return arr.get(index);
    }

    // Converte um literal de caractere da AST em um CharValue
    @Override
    public Object visitExpChar(ExpChar exp) {
        return new CharValue(exp.value);
    }

    // Avalia o acesso a um campo de um 'data' (ex: p.x)
    @Override
    public Object visitExpField(ExpField exp) {
        Value target = (Value) exp.target.accept(this);

        if (target == null || target instanceof NullValue) {
            return new NullValue(); // Acessar campo de 'null' resulta em 'null'
        }

        if (target instanceof DataValue) {
            return ((DataValue) target).getField(exp.field);
        }

        throw new RuntimeException("Tentativa de aceder a um campo de um valor que não é um 'data'.");
    }

    // Converte um literal de ponto flutuante da AST em um FloatValue
    @Override
    public Object visitExpFloat(ExpFloat exp) {
        return new FloatValue(exp.value);
    }

    // Avalia o acesso a um índice de um array (ex: v[i])
    @Override
    public Object visitExpIndex(ExpIndex exp) {
        Value target = (Value) exp.target.accept(this);
        Value indexVal = (Value) exp.index.accept(this);

        if (target instanceof ArrayValue && indexVal instanceof IntValue) {
            int index = ((IntValue) indexVal).getValue();
            Value result = ((ArrayValue) target).get(index);

            // Se o índice for válido mas não houver valor, retorna NullValue
            if (result == null) {
                return new NullValue();
            }
            return result;
        }
        throw new RuntimeException("Tentativa de indexar um valor que não é um array com um índice inteiro.");
    }

    // Converte um literal inteiro da AST em um IntValue
    @Override
    public Object visitExpInt(ExpInt exp) {
        return new IntValue(exp.value);
    }
    
    // Avalia uma expressão 'new', criando um ArrayValue ou um DataValue
    @Override
    public Object visitExpNew(ExpNew exp) {
        if (exp.size != null) {
            // Criação de array (ex: new Int[10])
            Value sizeValue = (Value) exp.size.accept(this);
            if (sizeValue instanceof IntValue) {
                return new ArrayValue(((IntValue) sizeValue).getValue());
            }
            throw new RuntimeException("O tamanho de um novo array deve ser um inteiro.");
        }

        // Criação de 'data' (ex: new Ponto)
        String typeName = exp.type.baseType;
        if (memory.hasDataDef(typeName)) {
            DataRegular dataDef = (DataRegular) memory.getDataDef(typeName);
            return new DataValue(dataDef);
        }

        throw new RuntimeException("Tipo '" + typeName + "' não definido para a operação 'new'.");
    }

    // Converte o literal 'null' da AST em um NullValue
    @Override
    public Object visitExpNull(ExpNull exp) {
        return new NullValue();
    }

    // Avalia uma expressão entre parênteses, simplesmente visitando a expressão interna
    @Override
    public Object visitExpParen(ExpParen exp) {
        return exp.exp.accept(this);
    }

    // Avalia uma expressão unária (ex: -a ou !b)
    @Override
    public Object visitExpUnaryOp(ExpUnaryOp exp) {
        Value value = (Value) exp.exp.accept(this);

        switch (exp.op) {
            case "!":
                if (value instanceof BoolValue) {
                    return new BoolValue(!((BoolValue) value).getValue());
                }
                throw new RuntimeException("Operador '!' só pode ser aplicado a booleanos.");
            case "-":
                if (value instanceof IntValue) {
                    return new IntValue(-((IntValue) value).getValue());
                }
                if (value instanceof FloatValue) {
                    return new FloatValue(-((FloatValue) value).getValue());
                }
                throw new RuntimeException("Operador '-' só pode ser aplicado a números.");
        }
        return null;
    }

    // Avalia uma variável, buscando seu valor na memória
    @Override
    public Object visitExpVar(ExpVar exp) {
        return memory.lookup(exp.name);
    }

    // Processa a definição de uma função, armazenando-a na memória para chamadas futuras. Esta é uma fase de registro, executada antes do 'main'
    @Override
    public Object visitFun(Fun fun) {
        memory.setFunction(fun.id, fun);
        return null;
    }

    // Método para o nó abstrato 'ItCond'. Delega a chamada para a implementação concreta (ItCondExpr ou ItCondLabelled)
    @Override
    public Object visitItCond(ItCond itCond) {
        // Delega a chamada para o tipo específico de ItCond
        return itCond.accept(this);
    }

    // Avalia a expressão dentro de uma condição de 'iterate' sem rótulo (ex: iterate(5))
    @Override
    public Object visitItCondExpr(ItCondExpr itCondExpr) {
        return itCondExpr.expression.accept(this);
    }

    // Avalia a expressão dentro de uma condição de 'iterate' com rótulo (ex: iterate(i: 5))
    @Override
    public Object visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return itCondLabelled.expression.accept(this);
    }

    // Método para o nó abstrato 'LValue'. Não é chamado diretamente
    @Override
    public Object visitLValue(LValue lValue) {
        return null;
    }

    // Avalia o acesso a um campo de 'data' (ex: p.x) quando este é usado em uma expressão (lado direito de uma atribuição)
    @Override
    public Object visitLValueField(LValueField lValueField) {
        Value objVal = (Value) lValueField.target.accept(this);

        if (objVal instanceof DataValue) {
            return ((DataValue) objVal).getField(lValueField.field);
        }
        throw new RuntimeException("Tentativa de acessar campo de algo que não é um 'data'.");
    }

    // Avalia uma variável (ex: x) quando esta é usada em uma expressão. Busca o valor da variável no escopo atual ou em escopos superiores
    @Override
    public Object visitLValueId(LValueId lValueId) {
        String varName = lValueId.id;
        // Diferente de `lookup`, aqui procuramos apenas no escopo atual para LValues.
        // A semântica pode variar, mas esta implementação é mais restrita.
        if (!memory.currentScope().containsKey(varName)) {
            throw new RuntimeException("Variável não inicializada: " + varName);
        }
        return memory.currentScope().get(varName);
    }

    // Avalia o acesso a um índice de array (ex: v[i]) quando este é usado em uma expressão
    @Override
    public Object visitLValueIndex(LValueIndex lValueIndex) {
        Value arrayVal = (Value) lValueIndex.target.accept(this);
        Value indexVal = (Value) lValueIndex.index.accept(this);

        if (arrayVal instanceof ArrayValue && indexVal instanceof IntValue) {
            int index = ((IntValue) indexVal).getValue();
            Value result = ((ArrayValue) arrayVal).get(index);
            
            // Se o índice for válido mas não houver valor, retorna NullValue
            if (result == null) {
                return new NullValue();
            }
            return result;
        }
        throw new RuntimeException("Indexação inválida.");
    }

    // Método para o nó de Parâmetro de função. Não realiza nenhuma ação durante a interpretação, pois os parâmetros são tratados em 'visitCmdCall'
    @Override
    public Object visitParam(Param param) {
        return null;
    }

    // Ponto de entrada do interpretador para um programa. Primeiro, registra todas as definições (funções e datas). Depois, encontra e executa a função 'main'
    @Override
    public Object visitProg(Prog prog) {

        // 1. Fase de registro de definições
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                visitFun((Fun) def);
            } else if (def instanceof DataRegular) {
                visitDataRegular((DataRegular) def);
            }
        }

        // 2. Fase de execução a partir da função 'main'
        Fun mainFunction = memory.getFunction("main");
        if (mainFunction != null) {
            mainFunction.body.accept(this); // Executa o corpo da main
        } else {
            throw new RuntimeException("Função 'main' não definida no programa.");
        }

        return null;
    }

    // Método para o nó de Tipo. Não possui ação na interpretação
    @Override
    public Object visitType(ast.Type type) {
        return null;
    }

    // Método auxiliar para extrair o nome base de um LValue (ex: de 'p.x' extrai 'p')
    private String extractVarName(LValue lval) {
        if (lval instanceof LValueId idLval) {
            return idLval.id;
        } else if (lval instanceof LValueIndex indexLval) {
            // Recursivamente pega a base da cadeia de índices
            return extractVarName(indexLval.target);
        }
        throw new UnsupportedOperationException("LValue não suportado: " + lval.getClass().getSimpleName());
    }

}
