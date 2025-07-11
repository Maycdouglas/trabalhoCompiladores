/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpNull extends Exp {
    @Override
    public String toDot(String parentId) {
        String id = "ExpNull" + hashCode();
        return String.format("\"%s\" [label=\"Null\"];\n\"%s\" -> \"%s\";\n", id, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpNull(this);
    }
}
