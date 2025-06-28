package ast;

import java.util.HashMap;


public class Sub extends Expr {
    private Expr left, right;

    public Sub(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }
}
