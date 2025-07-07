package interpreter;

import ast.*;

public class Interpreter {
    private final Memory memory = new Memory();

    public void interpret(Prog prog) {
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                Fun fun = (Fun) def;
                if (fun.id.equals("main")) {
                    execCmd(fun.body);
                }
            }
        }
    }

    private void execCmd(Cmd cmd) {
        if (cmd instanceof CmdAssign) {
            CmdAssign assign = (CmdAssign) cmd;
            String var = ((LValueId) assign.target).id;
            Value value = evalExp(assign.expression);
            memory.assign(var, value);
        } else if (cmd instanceof CmdPrint) {
            CmdPrint print = (CmdPrint) cmd;
            Value value = evalExp(print.value);
            System.out.println(value);
        } else if (cmd instanceof CmdBlock) {
            CmdBlock block = (CmdBlock) cmd;
            for (Cmd c : block.cmds) {
                execCmd(c);
            }
        } else {
            throw new RuntimeException("Comando não suportado: " + cmd.getClass());
        }
    }

    private Value evalExp(Exp exp) {
        if (exp instanceof ExpInt) {
            return new IntValue(((ExpInt) exp).value);
        } else if (exp instanceof ExpVar) {
            return memory.lookup(((ExpVar) exp).name);
        } else {
            throw new RuntimeException("Expressão não suportada: " + exp.getClass());
        }
    }
}
