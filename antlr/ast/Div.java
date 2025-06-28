// Div.java
package ast;

import java.util.HashMap;

public class Div extends BinOP {
    public Div(int line, int col, Expr left, Expr right) {
        super(line, col, left, right);
    }

    @Override
    public Object interpret(HashMap<String, Object> mem) {
        return (int) getLeft().interpret(mem) / (int) getRight().interpret(mem);
    }
}