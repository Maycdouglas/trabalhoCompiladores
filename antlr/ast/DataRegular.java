/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

public class DataRegular extends Data {
    public final List<Decl> declarations;

    public DataRegular(String name, List<Decl> declarations) {
        super(name);
        this.declarations = declarations;
    }

    @Override
    public String toDot(String parentId) {
        String id = "DataRegular" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"DataRegular: %s\"];\n", id, name));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        for (Decl d : declarations) {
            sb.append(d.toDot(id));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDataRegular(this);
    }

}