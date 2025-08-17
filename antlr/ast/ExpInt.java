/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpInt extends Exp implements DotPrintable {
    public final int value;
    public final int line;

    public ExpInt(int value, int line) {
        this.value = value;
        this.line = line;
    }

    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpInt" + hashCode();
        return String.format("\"%s\" [label=\"Int: %d\"];\n\"%s\" -> \"%s\";\n", idNode, value, parentId, idNode);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpInt(this);
    }
}
