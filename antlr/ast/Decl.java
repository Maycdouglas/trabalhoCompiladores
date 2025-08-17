/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Declaração: nome e tipo
public class Decl implements DotPrintable, ASTNode {
    public final String id;
    public final Type type;
    public final int line;

    public Decl(String id, Type type, int line) {
        this.id = id;
        this.type = type;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "Decl" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Decl: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        sb.append(type.toDot(idNode));
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDecl(this);
    }
}