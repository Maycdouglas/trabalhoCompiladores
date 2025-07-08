package interpreter;

import ast.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InterpreterVisitor implements Visitor<Object> {

    private final Map<String, Object> memory = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    private Object visitLvalExprFromLValue(LValue lv) {
        if (lv instanceof LValueId) {
            return memory.get(((LValueId) lv).id);
        } else if (lv instanceof LValueIndex) {
            LValueIndex idx = (LValueIndex) lv;
            Object baseArray = visitLvalExprFromLValue(idx.target);
            if (baseArray instanceof Object[]) {
                Object indexObject = idx.index.accept(this);
                if (indexObject instanceof Integer) {
                    return ((Object[]) baseArray)[(Integer) indexObject];
                }
            }
        } else if (lv instanceof LValueField) {
            // Lógica para campos de struct virá aqui no futuro
        }
        throw new RuntimeException("Não é possível avaliar o LValue: " + lv.getClass().getSimpleName());
    }

    public Object evalExp(Exp exp) {
        if (exp instanceof ExpInt) {
            return ((ExpInt) exp).getValue();
        } else if (exp instanceof ExpVar) {
            String var = ((ExpVar) exp).getName();
            if (!memory.containsKey(var)) {
                throw new RuntimeException("Variável não declarada: " + var);
            }
            return memory.get(var);
        }
        throw new RuntimeException("Expressão não suportada ainda: " + exp.getClass().getSimpleName());
    }

    @Override
    public Object visitCmd(Cmd exp) {
        return null;
    }

    @Override
    public Object visitCmdAssign(CmdAssign cmd) {
        Object valueToAssign = cmd.expression.accept(this);

        // --- LÓGICA PARA ATRIBUIÇÃO EM ARRAY ---
        if (cmd.target instanceof LValueIndex) {
            LValueIndex lvalIndex = (LValueIndex) cmd.target;

            // Avalia o objeto base (deve ser um array)
            Object arrayObject = visitLvalExprFromLValue(lvalIndex.target);

            if (arrayObject instanceof Object[]) {
                // Avalia a expressão do índice
                Object indexObject = lvalIndex.index.accept(this);
                if (indexObject instanceof Integer) {
                    int index = (Integer) indexObject;
                    ((Object[]) arrayObject)[index] = valueToAssign; // Atribui o valor no índice
                } else {
                    throw new RuntimeException("Índice de array deve ser um inteiro.");
                }
            } else {
                throw new RuntimeException("Tentando indexar um valor que não é um array.");
            }
        }
        // --- LÓGICA ANTIGA PARA VARIÁVEIS SIMPLES ---
        else if (cmd.target instanceof LValueId) {
            String varName = ((LValueId) cmd.target).id;
            memory.put(varName, valueToAssign);
        } else {
            throw new UnsupportedOperationException(
                    "Tipo de atribuição não suportado: " + cmd.target.getClass().getSimpleName());
        }

        return null;
    }

    @Override
    public Object visitCmdBlock(CmdBlock cmd) {
        for (Cmd commandInBlock : cmd.cmds) {
            commandInBlock.accept(this); // Delega a execução para o comando específico
        }
        return null;
    }

    @Override
    public Object visitCmdCall(CmdCall cmd) {
        return null;

    }

    @Override
    public Object visitCmdIf(CmdIf cmd) {
        Object conditionValue = cmd.condition.accept(this);
        if (conditionValue instanceof Boolean && (Boolean) conditionValue) {
            cmd.thenBranch.accept(this);
        } else if (cmd.elseBranch != null) {
            cmd.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public Object visitCmdIterate(CmdIterate cmd) {
        while (true) {
            Object conditionValue = cmd.condition.accept(this);

            if (conditionValue instanceof Boolean && (Boolean) conditionValue) {
                cmd.body.accept(this);
            } else {
                break;
            }
        }
        return null;
    }

    @Override
    public Object visitCmdPrint(CmdPrint cmd) {
        Object value = cmd.value.accept(this);
        System.out.println(value);
        return null;
    }

    @Override
    public Object visitCmdRead(CmdRead cmd) {
        String varName = extractVarName(cmd.lvalue);

        System.out.print(" entrada> ");

        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            memory.put(varName, value);
        } else {
            String input = scanner.next();
            System.err.println(
                    "Aviso: Entrada '" + input + "' não é um inteiro. Variável '" + varName + "' não foi atualizada.");
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
        return null;
    }

    @Override
    public Object visitExpBinOp(ExpBinOp exp) {
        Object left = exp.left.accept(this);
        Object right = exp.right.accept(this);

        if (left instanceof Number && right instanceof Number) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();

            // Retorna Float se qualquer um dos operandos for Float
            boolean isFloat = (left instanceof Float || right instanceof Float);

            switch (exp.op) {
                case "+":
                    return isFloat ? (l + r) : (int) (l + r);
                case "-":
                    return isFloat ? (l - r) : (int) (l - r);
                case "*":
                    return isFloat ? (l * r) : (int) (l * r);
                case "/":
                    return l / r;
                case "%":
                    return isFloat ? (l % r) : (int) (l % r);
                case "==":
                    return l == r;
                case "!=":
                    return l != r;
                case "<":
                    return l < r;
            }
        }

        // --- Lógica para BOOLEANOS ---
        if (left instanceof Boolean && right instanceof Boolean) {
            boolean l = (Boolean) left;
            boolean r = (Boolean) right;
            switch (exp.op) {
                case "&&":
                    return l && r;
            }
        }

        throw new RuntimeException("Operação binária não suportada para os tipos: "
                + left.getClass().getSimpleName() + " " + exp.op + " " + right.getClass().getSimpleName());
    }

    @Override
    public Object visitExpBool(ExpBool exp) {
        return exp.value;
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
        return exp.value;
    }

    @Override
    public Object visitExpField(ExpField exp) {
        return null;
    }

    @Override
    public Object visitExpFloat(ExpFloat exp) {
        return exp.value;
    }

    @Override
    public Object visitExpIndex(ExpIndex exp) {
        Object arrayObject = exp.target.accept(this);

        if (arrayObject instanceof Object[]) {
            Object indexObject = exp.index.accept(this);
            if (indexObject instanceof Integer) {
                int index = (Integer) indexObject;
                return ((Object[]) arrayObject)[index]; // Retorna o valor do índice
            } else {
                throw new RuntimeException("Índice de array deve ser um inteiro.");
            }
        }
        throw new RuntimeException("Tentando indexar um valor que não é um array.");
    }

    @Override
    public Object visitExpInt(ExpInt exp) {
        return exp.value;
    }

    @Override
    public Object visitExpNew(ExpNew exp) {
        if (exp.size != null) {
            Object sizeValue = exp.size.accept(this);
            if (sizeValue instanceof Integer) {
                int size = (Integer) sizeValue;

                Object[] array = new Object[size];

                return array;
            } else {
                throw new RuntimeException("O tamanho de um novo array deve ser um inteiro.");
            }
        }

        throw new RuntimeException("A operação 'new' para tipos de objeto ainda não foi implementada.");
    }

    @Override
    public Object visitExpNull(ExpNull exp) {
        return null;
    }

    @Override
    public Object visitExpParen(ExpParen exp) {
        return exp.exp.accept(this);
    }

    @Override
    public Object visitExpUnaryOp(ExpUnaryOp exp) {
        Object value = exp.exp.accept(this);
        switch (exp.op) {
            case "!":
                if (value instanceof Boolean) {
                    return !(Boolean) value;
                }
                throw new RuntimeException("Operador '!' só pode ser aplicado a booleanos.");
            case "-":
                if (value instanceof Integer) {
                    return -(Integer) value;
                }
                if (value instanceof Float) {
                    return -(Float) value;
                }
                throw new RuntimeException("Operador '-' só pode ser aplicado a números.");
        }
        return null;
    }

    @Override
    public Object visitExpVar(ExpVar exp) {
        if (!memory.containsKey(exp.name)) {
            throw new RuntimeException("Variável não inicializada: " + exp.name);
        }
        return memory.get(exp.name);
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

    public Map<String, Object> getMemory() {
        return memory;
    }

}
