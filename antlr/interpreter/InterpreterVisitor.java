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

public class InterpreterVisitor implements Visitor<Object> {

    private final Memory memory = new Memory();
    private final Scanner scanner = new Scanner(System.in);
    private List<Value> returnValues = new ArrayList<>(); // Lista para armazenar valores retornados por funções
    // Deve ficar aqui, pois é temporário e só existe enquanto estamos processando uma função

    public InterpreterVisitor() {
        memory.pushScope();// Inicializa o escopo global
    }

    // POSSO VIR AQUI E ALTERAR PARA return cmd.accept(this); SE NECESSÁRIO
    @Override
    public Object visitCmd(Cmd cmd) {
        throw new UnsupportedOperationException("visitCmd deve ser chamado apenas em subclasses concretas.");
    }

    @Override
    public Object visitCmdAssign(CmdAssign cmd) {
        Value valueToAssign = (Value) cmd.expression.accept(this);

        if (cmd.target instanceof LValueIndex) {
            LValueIndex lvalIndex = (LValueIndex) cmd.target;
            String arrayName = extractVarName(lvalIndex.target);
            Value arrayVal = (Value) memory.lookup(arrayName);
            Value indexVal = (Value) lvalIndex.index.accept(this);

            if (arrayVal instanceof ArrayValue && indexVal instanceof IntValue) {
                int index = ((IntValue) indexVal).getValue();
                ((ArrayValue) arrayVal).set(index, valueToAssign);
            } else {
                throw new RuntimeException("Atribuição inválida em array.");
            }
        } else if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            memory.currentScope().put(varName, valueToAssign);
        } else {
            throw new UnsupportedOperationException("Tipo de atribuição não suportado.");
        }
        return null;
    }

    @Override
    public Object visitCmdBlock(CmdBlock cmd) {
        for (Cmd commandInBlock : cmd.cmds) {
            commandInBlock.accept(this);
        }
        return null;
    }

    @Override
    public Object visitCmdCall(CmdCall cmd) {
        Fun funDef = memory.getFunction(cmd.id);
        if (funDef == null) {
            throw new RuntimeException("Função '" + cmd.id + "' não definida.");
        }
        if (funDef.params.size() != cmd.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + cmd.id + "'.");
        }

        Map<String, Value> newScope = new HashMap<>();
        for (int i = 0; i < funDef.params.size(); i++) {
            String paramName = funDef.params.get(i).id;
            Value argValue = (Value) cmd.args.get(i).accept(this);
            newScope.put(paramName, argValue);
        }

        memory.pushScope(newScope);

        this.returnValues.clear();

        List<Value> returned = null;
        try {
            // Executa o corpo da função UMA vez e captura valores retornados via exceção
            funDef.body.accept(this);
        } catch (ReturnException re) {
            returned = re.getValues();
        } finally {
            memory.popScope();
        }

        if (cmd.rets != null && !cmd.rets.isEmpty()) {
            if (returned == null) {
                throw new RuntimeException("Função não retornou valores mas chamador esperava " + cmd.rets.size());
            }
            if (returned.size() != cmd.rets.size()) {
                throw new RuntimeException("Número de variáveis de retorno (" + cmd.rets.size()
                        + ") é diferente do número de valores retornados (" + returned.size() + ").");
            }
            for (int i = 0; i < cmd.rets.size(); i++) {
                LValue targetLval = cmd.rets.get(i);
                Value returnedValue = returned.get(i);
                if (targetLval instanceof LValueId) {
                    memory.currentScope().put(((LValueId) targetLval).id, returnedValue);
                    System.out.println("Atribuiu retorno à variável " + ((LValueId) targetLval).id + " = " + returnedValue);
                } else {
                    throw new UnsupportedOperationException("Atribuição de retorno múltiplo só suporta variáveis simples.");
                }
            }
        }

        return null;
    }

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

    @Override
public Object visitCmdIterate(CmdIterate cmd) {
    if (cmd.condition instanceof ItCondLabelled itCond) {
        // Laço com rótulo (ex: iterate(i: v))
        Value iterable = (Value) itCond.expression.accept(this);

        if (iterable instanceof ArrayValue array) {
            for (Value element : array.getValues()) {
                memory.currentScope().put(itCond.label, element);
                cmd.body.accept(this);
            }
        } else if (iterable instanceof IntValue intVal) {
            int max = intVal.getValue();
            for (int i = 0; i < max; i++) {
                memory.currentScope().put(itCond.label, new IntValue(i));
                cmd.body.accept(this);
            }
        } else {
            throw new UnsupportedOperationException("'iterate' com rótulo só suporta arrays ou inteiros.");
        }
    } else if (cmd.condition instanceof ItCondExpr itCondExpr) {
        // Laço sem rótulo (ex: iterate(nlines))
        Value iterable = (Value) itCondExpr.expression.accept(this);

        if (iterable instanceof IntValue intVal) {
            int max = intVal.getValue();
            memory.pushScope();
            try {
                for (int i = 0; i < max; i++) {
                    cmd.body.accept(this);
                }
            } finally {
                memory.popScope();
            }
        } else {
            throw new UnsupportedOperationException("'iterate' sem rótulo só suporta inteiros.");
        }
    } else {
        throw new UnsupportedOperationException("Tipo de condição de iterate não suportado.");
    }
    return null;
}


    @Override
    public Object visitCmdPrint(CmdPrint cmd) {
        Value valueToPrint = (Value) cmd.value.accept(this);
        System.out.print(valueToPrint.toString());
        return null;
    }

    @Override
    public Object visitCmdRead(CmdRead cmd) {
        String varName = extractVarName(cmd.lvalue);
        System.out.print(" entrada> ");

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

    // public class ReturnException extends RuntimeException {
    //     public final List<Value> values;
    //     public ReturnException(List<Value> values) {
    //         this.values = values;
    //     }
    // }

    @Override
    public Object visitCmdReturn(CmdReturn cmd) {
        List<Value> values = new ArrayList<>();
        for (Exp exp : cmd.values) {
            values.add((Value) exp.accept(this));
        }
        throw new ReturnException(values);
    }

    @Override
    public Object visitData(Data data) {
        if (data instanceof DataAbstract) {
            return visitDataAbstract((DataAbstract) data);
        } else if (data instanceof DataRegular) {
            return visitDataRegular((DataRegular) data);
        }
        throw new RuntimeException("Tipo de data desconhecido: " + data.getClass().getName());
    }

    @Override
    public Object visitDataAbstract(DataAbstract data) {
        // Armazena a definição do tipo abstrato
        memory.setDataDef(data.name, data);
        // Também registrar funções internas associadas ao tipo
        for (Fun fun : data.functions) {
            memory.setFunction(fun.id, fun);
        }
        return null;
    }

    @Override
    public Object visitDataRegular(DataRegular data) {
        // Armazena a definição do tipo regular
        memory.setDataDef(data.name, data);
        return null;
    }

    @Override
    public Object visitDecl(Decl decl) {
        return null;
    }

    @Override
    public Object visitDef(Def def) {
        return null;
    }

    @Override
    public Object visitExp(Exp exp) {
        return exp.accept(this);
    }

    @Override
    public Object visitExpBinOp(ExpBinOp exp) {
        Value left = (Value) exp.left.accept(this);
        Value right = (Value) exp.right.accept(this);

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

        // Comparação entre CharValue
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

        // Coerção automática de IntValue para BoolValue, apenas para && e ||
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

    @Override
    public Object visitExpBool(ExpBool exp) {
        return new BoolValue(exp.value);
    }

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
            // executa o corpo da função; caso um return ocorra, será jogada ReturnException
            funDef.body.accept(this);
        } catch (ReturnException re) {
            returned = re.getValues();
        } finally {
            // garante que a stack de escopos seja restaurada
            memory.popScope();
        }

        // se não houve return, devolve lista vazia (ou adapte conforme sua semântica)
        return (returned != null) ? returned : new ArrayList<Value>();
    }

    @Override
    public Object visitExpCallIndexed(ExpCallIndexed exp) {
        // Avalia a chamada da função, espera-se uma lista de valores
        Object result = exp.call.accept(this);
        if (!(result instanceof List<?>)) {
            throw new RuntimeException("Function call did not return a list: " + result);
        }
        List<?> results = (List<?>) result;

        // Avalia o índice
        Object indexObj = exp.index.accept(this);
        if (!(indexObj instanceof IntValue intVal)) {
            throw new RuntimeException("Index must evaluate to an integer.");
        }
        int index = intVal.getValue();

        // Verifica se o índice está dentro dos limites
        if (index < 0 || index >= results.size()) {
            throw new RuntimeException("Index out of bounds in ExpCallIndexed: " + index);
        }

        // Retorna o valor específico da lista
        Object value = results.get(index);
        if (!(value instanceof Value)) {
            throw new RuntimeException("Expected a Value at index " + index);
        }

        return value;
    }



    @Override
    public Object visitExpChar(ExpChar exp) {
        return new CharValue(exp.value);
    }

    @Override
    public Object visitExpField(ExpField exp) {
        Value target = (Value) exp.target.accept(this);

        if (target instanceof DataValue) {
            return ((DataValue) target).getField(exp.field);
        }

        throw new RuntimeException("Tentativa de aceder a um campo de um valor que não é um 'data'.");
    }

    @Override
    public Object visitExpFloat(ExpFloat exp) {
        return new FloatValue(exp.value);
    }

    @Override
    public Object visitExpIndex(ExpIndex exp) {
        Value target = (Value) exp.target.accept(this);
        Value indexVal = (Value) exp.index.accept(this);

        if (target instanceof ArrayValue && indexVal instanceof IntValue) {
            int index = ((IntValue) indexVal).getValue();
            return ((ArrayValue) target).get(index);
        }
        throw new RuntimeException("Tentativa de indexar um valor que não é um array com um índice inteiro.");
    }

    @Override
    public Object visitExpInt(ExpInt exp) {
        return new IntValue(exp.value);
    }

    @Override
    public Object visitExpNew(ExpNew exp) {
        if (exp.size != null) {
            Value sizeValue = (Value) exp.size.accept(this);
            if (sizeValue instanceof IntValue) {
                return new ArrayValue(((IntValue) sizeValue).getValue());
            }
            throw new RuntimeException("O tamanho de um novo array deve ser um inteiro.");
        }

        String typeName = exp.type.baseType;
        if (memory.hasDataDef(typeName)) {
            DataRegular dataDef = (DataRegular) memory.getDataDef(typeName);
            return new DataValue(dataDef);
        }

        throw new RuntimeException("Tipo '" + typeName + "' não definido para a operação 'new'.");
    }

    @Override
    public Object visitExpNull(ExpNull exp) {
        return new NullValue();
    }

    @Override
    public Object visitExpParen(ExpParen exp) {
        return exp.exp.accept(this);
    }

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

    @Override
    public Object visitExpVar(ExpVar exp) {
        return memory.lookup(exp.name);
    }

    @Override
    public Object visitFun(Fun fun) {
        memory.setFunction(fun.id, fun);
        return null;
    }

    @Override
    public Object visitItCond(ItCond itCond) {
        // Delega a chamada para o tipo específico de ItCond
        return itCond.accept(this);
    }

    @Override
    public Object visitItCondExpr(ItCondExpr itCondExpr) {
        return itCondExpr.expression.accept(this);
    }

    @Override
    public Object visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return itCondLabelled.expression.accept(this);
    }

    @Override
    public Object visitLValue(LValue lValue) {
        return null;
    }

    @Override
    public Object visitLValueField(LValueField lValueField) {
        Value objVal = (Value) lValueField.target.accept(this);

        if (objVal instanceof DataValue) {
            return ((DataValue) objVal).getField(lValueField.field);
        }
        throw new RuntimeException("Tentativa de acessar campo de algo que não é um 'data'.");
    }

    @Override
    public Object visitLValueId(LValueId lValueId) {
        String varName = lValueId.id;
        if (!memory.currentScope().containsKey(varName)) {
            throw new RuntimeException("Variável não inicializada: " + varName);
        }
        return memory.currentScope().get(varName);
    }

    @Override
    public Object visitLValueIndex(LValueIndex lValueIndex) {
        Value arrayVal = (Value) lValueIndex.target.accept(this);
        Value indexVal = (Value) lValueIndex.index.accept(this);

        if (arrayVal instanceof ArrayValue && indexVal instanceof IntValue) {
            int index = ((IntValue) indexVal).getValue();
            return ((ArrayValue) arrayVal).get(index);
        }
        throw new RuntimeException("Indexação inválida.");
    }

    @Override
    public Object visitParam(Param param) {
        return null;
    }

    @Override
    public Object visitProg(Prog prog) {
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                visitFun((Fun) def);
            } else if (def instanceof DataRegular) {
                visitDataRegular((DataRegular) def);
            }
        }

        Fun mainFunction = memory.getFunction("main");
        if (mainFunction != null) {
            mainFunction.body.accept(this);
        } else {
            throw new RuntimeException("Função 'main' não definida no programa.");
        }

        return null;
    }

    @Override
    public Object visitType(ast.Type type) {
        return null;
    }

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
