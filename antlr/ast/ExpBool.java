/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpBool extends Exp {
    public final boolean value;

    public ExpBool(boolean value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpBool" + hashCode();
        return String.format("\"%s\" [label=\"Bool: %s\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpBool(this);
    }
}
