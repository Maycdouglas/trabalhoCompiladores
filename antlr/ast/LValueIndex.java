package ast;

public class LValueIndex extends LValue {
    public final LValue target;
    public final Exp index;

    public LValueIndex(LValue target, Exp index) {
        this.target = target;
        this.index = index;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "LValueIndex" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"LValueIndex\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(target.toDot(idNode));
        sb.append(index.toDot(idNode));
        return sb.toString();
    }
}
