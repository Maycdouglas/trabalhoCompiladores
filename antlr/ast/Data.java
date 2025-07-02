package ast;

// Data Abstract e Regular
public abstract class Data implements Def {
    public final String name;

    public Data(String name) {
        this.name = name;
    }
}