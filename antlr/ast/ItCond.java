/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Condição de iteração base (para 'iterate')
public abstract class ItCond implements DotPrintable, ASTNode  {
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitItCond(this);
    }
}
