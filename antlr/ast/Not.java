package ast;

import java.util.HashMap;


public class Not extends Expr {
    private Expr expr;

    public Not(int line, int col, Expr expr) {
        super(line, col);
        this.expr = expr;
    }
}
