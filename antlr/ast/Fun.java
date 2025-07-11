/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

// Função
public class Fun implements Def, DotPrintable {
    public final String id;
    public final List<Param> params;
    public final List<Type> retTypes; // pode ser vazio
    public final Cmd body;

    public Fun(String id, List<Param> params, List<Type> retTypes, Cmd body) {
        this.id = id;
        this.params = params;
        this.retTypes = retTypes;
        this.body = body;
    }

     @Override
    public String toDot(String parentId) {
        String idNode = "Fun" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Fun: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        for (Param p : params) {
            sb.append(p.toDot(idNode));
        }
        for (Type t : retTypes) {
            sb.append(t.toDot(idNode));
        }
        if (body instanceof DotPrintable) {
            sb.append(((DotPrintable) body).toDot(idNode));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFun(this);
    }
}