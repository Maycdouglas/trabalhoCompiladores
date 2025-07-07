package ast;

import interpreter.Visitor;

public class ExpNull extends Exp {
    @Override
    public String toDot(String parentId) {
        String id = "ExpNull" + hashCode();
        return String.format("\"%s\" [label=\"Null\"];\n\"%s\" -> \"%s\";\n", id, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpNull(this);
    }
}
