package ast;

public class ExpChar extends Exp {
    public final char value;

    public ExpChar(char value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpChar" + hashCode();
        return String.format("\"%s\" [label=\"Char: %s\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }
}
