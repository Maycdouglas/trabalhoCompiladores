/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Interface Def: para ser implementada por Data e Fun
public interface Def extends ASTNode {

    @Override
    public <T> T accept(Visitor<T> visitor);
}