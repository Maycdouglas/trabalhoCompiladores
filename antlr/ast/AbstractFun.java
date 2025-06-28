package ast;

import java.util.HashMap;

public class AbstractFun extends Node {
    private String name;
    private Type type;

    public AbstractFun(int line, int col, String name, Type type) {
        super(line, col);
        this.name = name;
        this.type = type;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}
