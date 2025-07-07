package ast;

import interpreter.Visitor;

public class ExpBinOp extends Exp {
    public final String op;
    public final Exp left;
    public final Exp right;

    public ExpBinOp(String op, Exp left, Exp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpBinOp" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Op: %s\"];\n", id, op));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(left.toDot(id));
        sb.append(right.toDot(id));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpBinOp(this);
    }
}

