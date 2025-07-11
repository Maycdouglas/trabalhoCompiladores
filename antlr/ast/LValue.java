/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public abstract class LValue extends Exp implements ASTNode {
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLValue(this);
    }
}
