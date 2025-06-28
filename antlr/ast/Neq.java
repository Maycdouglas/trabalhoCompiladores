package ast;

import java.util.HashMap;


public class Neq extends Expr {
    private Expr left, right;

    public Neq(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }
}
