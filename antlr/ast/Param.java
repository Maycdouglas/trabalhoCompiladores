package ast;

// Parâmetro
public class Param implements DotPrintable {
    public final String id;
    public final Type type;

    public Param(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "Param" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Param: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(type.toDot(idNode));
        return sb.toString();
    }
}