package ast;

import java.util.HashMap;

public class CharExpr extends Expr {
    private String value;

    public CharExpr(int line, int col, String value) {
        super(line, col);
        this.value = value;

    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}
