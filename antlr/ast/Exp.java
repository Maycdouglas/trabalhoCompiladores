/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public abstract class Exp extends Cmd implements DotPrintable, ASTNode {
    private static int nextId = 0;

    public Type expType;

    protected static String getNextId() {
        return "node" + (nextId++);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExp(this);
    }
}
