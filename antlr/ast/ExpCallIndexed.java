package ast;

public class ExpCallIndexed extends Exp {
    public final ExpCall call;
    public final Exp index;

    public ExpCallIndexed(ExpCall call, Exp index) {
        this.call = call;
        this.index = index;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpCallIndexed" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Call[]\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(call.toDot(idNode));
        sb.append(index.toDot(idNode));
        return sb.toString();
    }
}
