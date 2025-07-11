/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

public class ExpCall extends Exp {
    public final String id;
    public final List<Exp> args;

    public ExpCall(String id, List<Exp> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpCall" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Call: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        for (Exp arg : args) {
            sb.append(arg.toDot(idNode));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpCall(this);
    }
}
