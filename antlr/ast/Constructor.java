package ast;

import java.util.HashMap;

public class Constructor extends Node {
    private String name;
    private Constructor next;

    public Constructor(int line, int col, String name) {
        super(line, col);
        this.name = name;
    }

    public Constructor(int line, int col, Constructor prev, Constructor next) {
        super(line, col);
        this.name = null; // placeholder
        this.next = next;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("Constructor not yet implemented");
    }
}
