package ast;

public class ItCondExpr extends ItCond {
    public final Exp expression;

    public ItCondExpr(Exp expression) {
        this.expression = expression;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ItCondExpr" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"ItCondExpr\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(expression.toDot(idNode));
        return sb.toString();
    }
}
