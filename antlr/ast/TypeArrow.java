package ast;

import java.util.HashMap;

public class TypeArrow extends Type {
    private Type from, to;

    public TypeArrow(int line, int col, Type from, Type to) {
        super(line, col);
        this.from = from;
        this.to = to;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("TypeArrow not yet implemented");
    }
}
