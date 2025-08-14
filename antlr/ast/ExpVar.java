/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpVar extends Exp {
    public final String name;
    public final int line;

    public ExpVar(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return this.name;
    }

    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpVar" + hashCode();
        return String.format("\"%s\" [label=\"Var: %s\"];\n\"%s\" -> \"%s\";\n", idNode, name, parentId, idNode);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpVar(this);
    }
}
