/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Tipo base (simplificado)
public class Type implements DotPrintable, ASTNode{
    public final String baseType; // exemplo: Int, Bool, ou nome do tipo (TYID)
    public final int arrayDim;    // para representar [] (quantos níveis)

    public Type(String baseType, int arrayDim) {
        this.baseType = baseType;
        this.arrayDim = arrayDim;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "Type" + hashCode();
        String label = baseType + "[]".repeat(arrayDim);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Type: %s\"];\n", idNode, label));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitType(this);
    }
}