package interpreter;

import ast.*;
import java.util.HashMap;
import java.util.Map;

public class InterpreterVisitor implements Visitor<Object> {

    private final Map<String, Object> memory = new HashMap<>();

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
        Object value = cmd.expression.accept(this);
        String varName = extractVarName(cmd.target);
        memory.put(varName, value);
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

        // (Pode ser estendida para Float)
        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;
            switch (exp.op) {
                case "+":
                    return l + r;
                case "-":
                    return l - r;
                case "*":
                    return l * r;
                case "/":
                    return l / r; // Divisão inteira
                case "==":
                    return l == r;
                case "!=":
                    return l != r;
                case "<":
                    return l < r;
            }
        }

        // --- Lógica para operações com BOOLEANOS ---
        if (left instanceof Boolean && right instanceof Boolean) {
            boolean l = (Boolean) left;
            boolean r = (Boolean) right;
            switch (exp.op) {
                case "&&":
                    return l && r;
            }
        }

        // --- Lançar erro se a operação não for suportada para os tipos ---
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
        return null;
    }

    @Override
    public Object visitExpField(ExpField exp) {
        return null;
    }

    @Override
    public Object visitExpFloat(ExpFloat exp) {
        return null;
    }

    @Override
    public Object visitExpIndex(ExpIndex exp) {
        return null;
    }

    @Override
    public Object visitExpInt(ExpInt exp) {
        return exp.value;
    }

    @Override
    public Object visitExpNew(ExpNew exp) {
        return null;
    }

    @Override
    public Object visitExpNull(ExpNull exp) {
        return null;
    }

    @Override
    public Object visitExpParen(ExpParen exp) {
        return null;
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
        return null;
    }

    @Override
    public Object visitItCondExpr(ItCondExpr itCondExpr) {
        return null;
    }

    @Override
    public Object visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return null;
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
