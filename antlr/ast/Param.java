package ast;

// Parâmetro
public class Param {
    public final String id;
    public final Type type;

    public Param(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}