package ast;

public class CmdIterate extends Cmd implements DotPrintable {
    public final ItCond condition;
    public final Cmd body;

    public CmdIterate(ItCond condition, Cmd body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toDot(String parentId) {
        String id = "CmdIterate" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Iterate\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(condition.toDot(id));
        sb.append(body instanceof DotPrintable ? ((DotPrintable) body).toDot(id) : "");
        return sb.toString();
    }
}
