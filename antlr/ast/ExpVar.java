package ast;

public class ExpVar extends Exp {
    public final String name;

    public ExpVar(String name) {
        this.name = name;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpVar" + hashCode();
        return String.format("\"%s\" [label=\"Var: %s\"];\n\"%s\" -> \"%s\";\n", idNode, name, parentId, idNode);
    }
}
