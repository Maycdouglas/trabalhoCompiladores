package ast;

import interpreter.Visitor;

public class ExpFloat extends Exp {
    public final float value;

    public ExpFloat(float value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpFloat" + hashCode();
        return String.format("\"%s\" [label=\"Float: %.2f\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpFloat(this);
    }
}
