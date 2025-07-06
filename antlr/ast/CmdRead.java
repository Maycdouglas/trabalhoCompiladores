package ast;

public class CmdRead extends Cmd implements DotPrintable {
    public final LValue lvalue;

    public CmdRead(LValue lvalue) {
        this.lvalue = lvalue;
    }

    @Override
    public String toDot(String parentId) {
        String id = "CmdRead" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Read\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(lvalue.toDot(id));
        return sb.toString();
    }
}
