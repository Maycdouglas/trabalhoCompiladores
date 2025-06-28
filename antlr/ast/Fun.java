package ast;

import java.util.HashMap;

public class Fun extends Node {
    private String name;
    private ParamList params;
    private Expr body;

    public Fun(int line, int col, String name, ParamList params, Expr body) {
        super(line, col);
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public ParamList getParams() {
        return params;
    }

    public Expr getBody() {
        return body;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("Fun cannot be interpreted directly");
    }
}