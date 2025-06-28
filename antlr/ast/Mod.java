package ast;

import java.util.HashMap;

public class Mod extends Expr {
    private Expr left, right;

    public Mod(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }
}
