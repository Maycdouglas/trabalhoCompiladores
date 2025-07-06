package ast;

public class ExpInt extends Exp {
    public final int value;

    public ExpInt(int value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpInt" + hashCode();
        return String.format("\"%s\" [label=\"Int: %d\"];\n\"%s\" -> \"%s\";\n", idNode, value, parentId, idNode);
    }
}
