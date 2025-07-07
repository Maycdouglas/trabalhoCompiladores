package ast;

import interpreter.Visitor;

public class ExpParen extends Exp {
    public final Exp exp;

    public ExpParen(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpParen" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Paren\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(exp.toDot(id));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpParen(this);
    }
}
