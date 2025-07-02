package ast;

// Programa: contém lista de definições (data e funções)
import java.util.List;

public class Prog {
    public final List<Def> definitions;

    public Prog(List<Def> definitions) {
        this.definitions = definitions;
    }
}