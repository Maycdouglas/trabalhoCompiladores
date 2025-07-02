package ast;

// Tipo base (simplificado)
public class Type {
    public final String baseType; // exemplo: Int, Bool, ou nome do tipo (TYID)
    public final int arrayDim;    // para representar [] (quantos níveis)

    public Type(String baseType, int arrayDim) {
        this.baseType = baseType;
        this.arrayDim = arrayDim;
    }
}