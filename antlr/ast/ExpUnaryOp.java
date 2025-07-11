/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpUnaryOp extends Exp {
    public final String op;
    public final Exp exp;

    public ExpUnaryOp(String op, Exp exp) {
        this.op = op;
        this.exp = exp;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpUnaryOp" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Unary: %s\"];\n", id, op));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        sb.append(exp.toDot(id));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpUnaryOp(this);
    }
}
