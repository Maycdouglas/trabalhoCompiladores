/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;
import java.util.List;

public class Prog implements DotPrintable, ASTNode {
    public final List<Def> definitions;
    public final int line;

    public Prog(List<Def> definitions, int line) {
        this.definitions = definitions;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parentId) {
        String id = "Prog" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Prog\"];\n", id));
        if (parentId != null)
            sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        for (Def def : definitions) {
            if (def instanceof DotPrintable)
                sb.append(((DotPrintable) def).toDot(id));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProg(this);
    }
}