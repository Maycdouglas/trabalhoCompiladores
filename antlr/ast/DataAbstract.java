package ast;

public class DataAbstract extends Data {
    public final List<Decl> declarations;
    public final List<Fun> functions;

    public DataAbstract(String name, List<Decl> declarations, List<Fun> functions) {
        super(name);
        this.declarations = declarations;
        this.functions = functions;
    }
}