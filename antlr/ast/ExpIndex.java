/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpIndex extends Exp {
    public Exp target;
    public Exp index;
    public final int line;

    public ExpIndex(Exp target, Exp index, int line) {
        this.target = target;
        this.index = index;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parent) {
        String id = getNextId();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s [label=\"ExpIndex\"];\n", id));
        sb.append(String.format("%s -> %s;\n", parent, id));

        sb.append(target.toDot(id));
        sb.append(index.toDot(id));

        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpIndex(this);
    }
}