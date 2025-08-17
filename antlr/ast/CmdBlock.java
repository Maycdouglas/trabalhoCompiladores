/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

public class CmdBlock extends Cmd implements DotPrintable {
    public final List<Cmd> cmds;
    public final int line;

    public CmdBlock(List<Cmd> cmds, int line) {
        this.cmds = cmds;
        this.line = line;
    }

    @Override
    public int getLine() {
        return cmds.isEmpty() ? -1 : cmds.get(0).getLine();
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
