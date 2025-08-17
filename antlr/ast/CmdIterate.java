/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class CmdIterate extends Cmd implements DotPrintable {
    public final ItCond condition;
    public final Cmd body;
    public final int line;

    public CmdIterate(ItCond condition, Cmd body, int line) {
        this.condition = condition;
        this.body = body;
        this.line = line;
    }

    @Override
    public int getLine() {
        return line;
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdIterate(this);
    }
}
