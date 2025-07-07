package ast;

import interpreter.Visitor;
import java.util.List;

public class CmdBlock extends Cmd implements DotPrintable {
    public final List<Cmd> cmds;

    public CmdBlock(List<Cmd> cmds) {
        this.cmds = cmds;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "CmdBlock" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Block\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        for (Cmd cmd : cmds) {
            if (cmd instanceof DotPrintable) {
                sb.append(((DotPrintable) cmd).toDot(idNode));
            }
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdBlock(this);
    }
}
