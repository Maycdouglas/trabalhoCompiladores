package ast;

import interpreter.Visitor;

// Programa: contém lista de definições (data e funções)
// Representa o programa inteiro, com uma lista de definições
import java.util.List;

public class Prog implements DotPrintable, ASTNode{
    public final List<Def> definitions;

    public Prog(List<Def> definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toDot(String parentId) {
        String id = "Prog" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Prog\"];\n", id));
        if (parentId != null)
            sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, id));
        for (Def def : definitions) {
            if (def instanceof DotPrintable)
                sb.append(((DotPrintable) def).toDot(id));
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProg(this);
    }

}