package ast;

import interpreter.Visitor;

public class ExpBool extends Exp {
    public final boolean value;

    public ExpBool(boolean value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpBool" + hashCode();
        return String.format("\"%s\" [label=\"Bool: %s\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpBool(this);
    }
}
