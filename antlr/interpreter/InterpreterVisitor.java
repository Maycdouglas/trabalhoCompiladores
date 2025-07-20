/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import ast.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class InterpreterVisitor implements Visitor<Object> {

    private final Stack<Map<String, Value>> memoryStack = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);

    public InterpreterVisitor() {
        memoryStack.push(new HashMap<>());
    }

    private Map<String, Value> currentScope() {
        return memoryStack.peek();
    }

    @Override
    public Object visitCmd(Cmd exp) {
        return null;
    }

    @Override
    public Object visitCmdAssign(CmdAssign cmd) {
        Value valueToAssign = (Value) cmd.expression.accept(this);

        if (cmd.target instanceof LValueIndex) {
            LValueIndex lvalIndex = (LValueIndex) cmd.target;
            String arrayName = extractVarName(lvalIndex.target);
            Value arrayVal = (Value) currentScope().get(arrayName);
            Value indexVal = (Value) lvalIndex.index.accept(this);

            if (arrayVal instanceof ArrayValue && indexVal instanceof IntValue) {
                int index = ((IntValue) indexVal).getValue();
                ((ArrayValue) arrayVal).set(index, valueToAssign);
            } else {
                throw new RuntimeException("Atribuição inválida em array.");
            }
        } else if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            currentScope().put(varName, valueToAssign);
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
        while (true) {
            Value conditionValue = (Value) cmd.condition.accept(this);

            if (conditionValue instanceof BoolValue && ((BoolValue) conditionValue).getValue()) {
                cmd.body.accept(this);
            } else {
                break;
            }
        }
        return null;
    }

    @Override
    public Object visitCmdPrint(CmdPrint cmd) {
        Value valueToPrint = (Value) cmd.value.accept(this);
        System.out.println(valueToPrint.toString());
        return null;
    }

    @Override
    public Object visitCmdRead(CmdRead cmd) {
        String varName = extractVarName(cmd.lvalue);
        System.out.print(" entrada> ");

        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            currentScope().put(varName, new IntValue(value));
        } else if (scanner.hasNextFloat()) { // Adicionando suporte a float na leitura
            float value = scanner.nextFloat();
            currentScope().put(varName, new FloatValue(value));
        } else {
            String input = scanner.next();
            System.err.println(
                    "Aviso: Entrada '" + input + "' não é um número. Variável '" + varName + "' não foi atualizada.");
        }
        return null;
    }

    @Override
    public Object visitCmdReturn(CmdReturn cmd) {
        return null;
    }

    @Override
    public Object visitData(Data data) {
        return null;
    }

    @Override
    public Object visitDataAbstract(DataAbstract data) {
        return null;
    }

    @Override
    public Object visitDataRegular(DataRegular data) {
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

        if (left instanceof BoolValue && right instanceof BoolValue) {
            boolean l = ((BoolValue) left).getValue();
            boolean r = ((BoolValue) right).getValue();
            switch (exp.op) {
                case "&&":
                    return new BoolValue(l && r);
            }
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
        return null;
    }

    @Override
    public Object visitExpCallIndexed(ExpCallIndexed exp) {
        return null;
    }

    @Override
    public Object visitExpChar(ExpChar exp) {
        return new CharValue(exp.value);
    }

    @Override
    public Object visitExpField(ExpField exp) {
        return null;
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
        throw new RuntimeException("Tentando indexar um valor que não é um array com um índice inteiro.");
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
                int size = ((IntValue) sizeValue).getValue();
                return new ArrayValue(size);
            } else {
                throw new RuntimeException("O tamanho de um novo array deve ser um inteiro.");
            }
        }
        throw new RuntimeException("A operação 'new' para tipos de objeto ainda não foi implementada.");
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
        if (!currentScope().containsKey(exp.name)) {
            throw new RuntimeException("Variável não inicializada: " + exp.name);
        }
        return currentScope().get(exp.name);
    }

    @Override
    public Object visitFun(Fun fun) {
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
        return null;
    }

    @Override
    public Object visitLValueId(LValueId lValueId) {
        return null;
    }

    @Override
    public Object visitLValueIndex(LValueIndex lValueIndex) {
        return null;
    }

    @Override
    public Object visitParam(Param param) {
        return null;
    }

    @Override
    public Object visitProg(Prog prog) {
        for (Def def : prog.definitions) {
            if (def instanceof Fun fun && fun.id.equals("main")) {
                if (fun.body instanceof CmdBlock block) {
                    for (Cmd cmd : block.cmds) {
                        cmd.accept(this);
                    }
                }
                break;
            }
        }
        return null;
    }

    @Override
    public Object visitType(ast.Type type) {
        // Pode apenas retornar null se não for necessário interpretar tipos ainda
        return null;
    }

    private String extractVarName(LValue lval) {
        if (lval instanceof LValueId idLval) {
            return idLval.id;
        }
        throw new UnsupportedOperationException("LValue não suportado: " + lval.getClass().getSimpleName());
    }

}
