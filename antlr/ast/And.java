// And.java
package ast;

import java.util.HashMap;

public class And extends Expr {
    private Expr left, right;

    public And(int line, int col, Expr left, Expr right) {
        super(line, col);
        this.left = left;
        this.right = right;
    }

}