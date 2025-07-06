package ast;

public class ExpIndex extends Exp {
    public Exp target;
    public Exp index;
    public ExpIndex(Exp target, Exp index) {
        this.target = target;
        this.index = index;
    }

    @Override
    public String toDot(String parent) {
        String id = getNextId();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s [label=\"ExpIndex\"];\n", id));
        sb.append(String.format("%s -> %s;\n", parent, id));

        sb.append(target.toDot(id));
        sb.append(index.toDot(id));

        return sb.toString();
    }
}