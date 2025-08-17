/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class LValueIndex extends LValue {
    public final LValue target;
    public final Exp index;

    public LValueIndex(LValue target, Exp index, int line) {
        this.target = target;
        this.index = index;
        this.line = line;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "LValueIndex" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"LValueIndex\"];\n", idNode));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(target.toDot(idNode));
        sb.append(index.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLValueIndex(this);
    }
}
