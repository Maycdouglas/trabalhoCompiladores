package ast;

import java.util.HashMap;

public abstract class Type extends Node {
    public Type(int line, int col) {
        super(line, col);
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}