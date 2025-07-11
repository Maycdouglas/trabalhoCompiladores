/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpFloat extends Exp {
    public final float value;

    public ExpFloat(float value) {
        this.value = value;
    }

    @Override
    public String toDot(String parentId) {
        String id = "ExpFloat" + hashCode();
        return String.format("\"%s\" [label=\"Float: %.2f\"];\n\"%s\" -> \"%s\";\n", id, value, parentId, id);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpFloat(this);
    }
}
