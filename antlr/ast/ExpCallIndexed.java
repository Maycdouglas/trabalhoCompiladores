/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpCallIndexed extends Exp {
    public final ExpCall call;
    public final Exp index;

    public ExpCallIndexed(ExpCall call, Exp index) {
        this.call = call;
        this.index = index;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpCallIndexed" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Call[]\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(call.toDot(idNode));
        sb.append(index.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpCallIndexed(this);
    }
}
