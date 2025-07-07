package ast;

import interpreter.Visitor;

public class ExpVar extends Exp {
    public final String name;

    public ExpVar(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpVar" + hashCode();
        return String.format("\"%s\" [label=\"Var: %s\"];\n\"%s\" -> \"%s\";\n", idNode, name, parentId, idNode);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpVar(this);
    }
}
