package ast;

import java.util.HashMap;

public class ParamList extends Node {
    private String param;
    private ParamList next;

    public ParamList(int line, int col, String param) {
        super(line, col);
        this.param = param;
    }

    public ParamList(int line, int col, String param, ParamList next) {
        super(line, col);
        this.param = param;
        this.next = next;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("ParamList not yet implemented");
    }
}
