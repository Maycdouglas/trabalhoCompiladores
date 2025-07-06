package ast;

public class ExpNew extends Exp {
    public final Type type;
    public final Exp size; // pode ser null se não houver [exp]

    public ExpNew(Type type, Exp size) {
        this.type = type;
        this.size = size;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpNew" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"New\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(type.toDot(id));
        if (size != null)
            sb.append(size.toDot(id));
        return sb.toString();
    }
}
