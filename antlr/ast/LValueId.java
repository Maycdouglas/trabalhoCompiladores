package ast;

public class LValueId extends LValue {
    public final String id;

    public LValueId(String id) {
        this.id = id;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "LValueId" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"LValueId: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        return sb.toString();
    }
}
