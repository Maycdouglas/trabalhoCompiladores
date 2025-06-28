package ast;

import java.util.HashMap;

public class DataDecl extends Node {
    private String typeName;
    private Constructor constructors;

    public DataDecl(int line, int col, String typeName, Constructor constructors) {
        super(line, col);
        this.typeName = typeName;
        this.constructors = constructors;
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("DataDecl not yet implemented");
    }
}
