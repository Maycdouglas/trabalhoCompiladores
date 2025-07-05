package ast;

// Declaração: nome e tipo
public class Decl implements DotPrintable{
    public final String id;
    public final Type type;

    public Decl(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "Decl" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Decl: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(type.toDot(idNode));
        return sb.toString();
    }
}