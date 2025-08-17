/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class CmdRead extends Cmd implements DotPrintable {
    public final LValue lvalue;
    public final int line;

    public CmdRead(LValue lvalue, int line) {
        this.lvalue = lvalue;
        this.line = line;
    }

    @Override
    public int getLine() {
        return line;
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmdRead(this);
    }
}
