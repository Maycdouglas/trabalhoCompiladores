/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ItCondExpr extends ItCond {
    public final Exp expression;
    public final int line;

    public ItCondExpr(Exp expression, int line) {
        this.expression = expression;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ItCondExpr" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"ItCondExpr\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(expression.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitItCondExpr(this);
    }
}
