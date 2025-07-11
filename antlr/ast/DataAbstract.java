/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

public class DataAbstract extends Data {
    public final List<Decl> declarations;
    public final List<Fun> functions;

    public DataAbstract(String name, List<Decl> declarations, List<Fun> functions) {
        super(name);
        this.declarations = declarations;
        this.functions = functions;
    }

    @Override
    public String toDot(String parentId) {
        String id = "DataAbstract" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"DataAbstract: %s\"];\n", id, name));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        for (Decl d : declarations) {
            sb.append(d.toDot(id));
        }
        for (Fun f : functions) {
            sb.append(f.toDot(id));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDataAbstract(this);
    }
}