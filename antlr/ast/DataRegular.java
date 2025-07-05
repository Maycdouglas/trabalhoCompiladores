package ast;

import java.util.List;

public class DataRegular extends Data {
    public final List<Decl> declarations;

    public DataRegular(String name, List<Decl> declarations) {
        super(name);
        this.declarations = declarations;
    }
}