package ast;

public class ExpBool extends Exp {
    public final boolean value;

    public ExpBool(boolean value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpBool" + hashCode();
        return String.format("\"%s\" [label=\"Bool: %s\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }
}
