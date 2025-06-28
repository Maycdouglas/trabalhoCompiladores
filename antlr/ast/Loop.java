package ast;

import java.util.HashMap;

public class Loop extends Node {
    private Expr condition;
    private Node body;

    public Loop(int line, int col, Expr condition, Node body) {
        super(line, col);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }
}
