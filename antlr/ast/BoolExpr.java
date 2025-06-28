package ast;

import java.util.HashMap;

public class BoolExpr extends Expr {
    private boolean value;

    public BoolExpr(int line, int col, boolean value) {
        super(line, col);
        this.value = value;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " not yet implemented");
    }

}
