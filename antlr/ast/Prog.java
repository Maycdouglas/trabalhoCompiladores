package ast;

// Programa: contém lista de definições (data e funções)
// Representa o programa inteiro, com uma lista de definições
import java.util.List;

public class Prog {
    public final List<Def> definitions;

    public Prog(List<Def> definitions) {
        this.definitions = definitions;
    }
}