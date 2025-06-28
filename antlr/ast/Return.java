package ast;

import java.util.HashMap;

public class Return extends Node {
    private Expr expr;

    public Return(int line, int col, Expr expr) {
        super(line, col);
        this.expr = expr;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " not yet implemented");
    }

}
