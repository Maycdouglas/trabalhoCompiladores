/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Comando base (futuramente vai herdar para os comandos específicos)
public abstract class Cmd implements ASTNode {

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmd(this);
    }
}