/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class CmdIf extends Cmd implements DotPrintable {
    public final Exp condition;
    public final Cmd thenBranch;
    public final Cmd elseBranch; // pode ser null
    public final int line;

    public CmdIf(Exp condition, Cmd thenBranch, Cmd elseBranch, int line) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
        this.line = line;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String toDot(String parentId) {
        String id = "CmdIf" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"If\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));

        sb.append(condition.toDot(id));
        sb.append(thenBranch instanceof DotPrintable ? ((DotPrintable) thenBranch).toDot(id) : "");
        if (elseBranch != null)
            sb.append(((DotPrintable) elseBranch).toDot(id));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdIf(this);
    }
}
