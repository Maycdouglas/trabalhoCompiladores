package ast;

import interpreter.Visitor;

public class ItCondLabelled extends ItCond {
    public final String label;
    public final Exp expression;

    public ItCondLabelled(String label, Exp expression) {
        this.label = label;
        this.expression = expression;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ItCondLabelled" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"ItCondLabelled: %s\"];\n", idNode, label));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(expression.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitItCondLabelled(this);
    }
}
