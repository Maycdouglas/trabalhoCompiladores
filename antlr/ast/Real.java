package ast;

import java.util.HashMap;

public class Real extends Expr {
    private double value;

    public Real(int line, int col, double value) {
        super(line, col);
        this.value = value;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}
