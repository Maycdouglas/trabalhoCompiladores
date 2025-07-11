/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class LValueField extends LValue {
    public final LValue target;
    public final String field;

    public LValueField(LValue target, String field) {
        this.target = target;
        this.field = field;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "LValueField" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"LValueField: %s\"];\n", idNode, field));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(target.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLValueField(this);
    }
}
