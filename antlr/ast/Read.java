package ast;

import java.util.HashMap;

public class Read extends Node {
    public Read(int line, int col) {
        super(line, col);
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}
