package ast;

// Declaração: nome e tipo
public class Decl {
    public final String id;
    public final Type type;

    public Decl(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}