/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class CmdPrint extends Cmd implements DotPrintable {
    public final Exp value;
    public final int line;

    public CmdPrint(Exp value, int line) {
        this.value = value;
        this.line = line;
    }

    @Override
    public int getLine() {
        return line;
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
