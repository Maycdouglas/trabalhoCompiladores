package ast;

import interpreter.Visitor;

// Data Abstract e Regular
public abstract class Data implements Def, DotPrintable {
    public final String name;

    public Data(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitData(this);
    }

}