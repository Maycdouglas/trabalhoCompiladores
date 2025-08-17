/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class CmdAssign extends Cmd implements DotPrintable {
    public final LValue target;
    public final Exp expression;
    public final int line;

    public CmdAssign(LValue target, Exp expression, int line) {
        this.target = target;
        this.expression = expression;
        this.line = line;
    }

    @Override
    public int getLine() {
        return target.getLine();
    }

    @Override
    public String toDot(String parentId) {
        String id = "CmdAssign" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Assign\"];\n", id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(target.toDot(id));
        sb.append(expression.toDot(id));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdAssign(this);
    }
}
