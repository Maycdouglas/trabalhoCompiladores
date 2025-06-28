package ast;

import java.util.HashMap;

public class Eq extends Expr {
    private Expr left, right;

    public Eq(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " not yet implemented");
    }

}
