package ast;

import interpreter.Visitor;

public class CmdPrint extends Cmd implements DotPrintable {
    public final Exp value;

    public CmdPrint(Exp value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "CmdPrint" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Print\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(value.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdPrint(this);
    }

}
