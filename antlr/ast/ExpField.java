/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpField extends Exp {
    public Exp target;
    public String field;
    public final int line;

    public ExpField(Exp target, String field, int line) {
        this.target = target;
        this.field = field;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parent) {
        String id = getNextId();
        String fieldId = getNextId();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s [label=\"ExpField\"];\n", id));
        sb.append(String.format("%s -> %s;\n", parent, id));

        sb.append(target.toDot(id));

        sb.append(String.format("%s [label=\"%s\"];\n", fieldId, field));
        sb.append(String.format("%s -> %s;\n", id, fieldId));

        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpField(this);
    }
}