/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Tipo base (simplificado)
public class Type implements DotPrintable, ASTNode {
    public final String baseType; // exemplo: Int, Bool, ou nome do tipo (TYID)
    public final int arrayDim; // para representar [] (quantos níveis)
    public final int line;
    public static final Type ERROR = new Type("<<error>>", 0, -1);

    public Type(String baseType, int arrayDim, int line) {
        this.baseType = baseType;
        this.arrayDim = arrayDim;
        this.line = line;
    }

    public Type(String baseType, int arrayDim) {
        this(baseType, arrayDim, -1); // Chama o outro construtor com line = 0
    }

    @Override
    public int getLine() {
        return this.line;
    }

    public boolean isNumeric() {
        return this.baseType.equals("Int") || this.baseType.equals("Float");
    }

    public boolean isFloat() {
        return this.baseType.equals("Float");
    }

    public boolean isBool() {
        return this.baseType.equals("Bool");
    }

    public boolean isNull() {
        return this.baseType.equals("Null");
    }

    public boolean isPrimitive() {
        return isNumeric() || isBool() || this.baseType.equals("Char");
    }

    public boolean isReference() {
        return this.arrayDim > 0 || !isPrimitive() && !isNull() && !isError();
    }

    public boolean isEquivalent(Type other) {
        if (this.isError() || other.isError()) {
            return true; // Evita erros em cascata
        }
        return this.baseType.equals(other.baseType) && this.arrayDim == other.arrayDim;
    }

    @Override
    public String toString() {
        if (arrayDim > 0) {
            return baseType + "[]".repeat(arrayDim);
        }
        return baseType;
    }

    public boolean isError() {
        return this.baseType.equals("<<error>>");
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