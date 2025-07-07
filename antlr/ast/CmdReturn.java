package ast;

import interpreter.Visitor;
import java.util.List;

public class CmdReturn extends Cmd implements DotPrintable {
    public final List<Exp> values;

    public CmdReturn(List<Exp> values) {
        this.values = values;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "CmdReturn" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Return\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        for (Exp e : values) {
            sb.append(e.toDot(idNode));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdReturn(this);
    }
}
