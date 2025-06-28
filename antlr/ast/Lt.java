package ast;

import java.util.HashMap;

public class Lt extends Expr {
    private Expr left, right;

    public Lt(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }
}
