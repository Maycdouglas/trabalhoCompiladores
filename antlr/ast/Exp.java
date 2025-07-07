package ast;

import interpreter.Visitor;

public abstract class Exp extends Cmd implements DotPrintable, ASTNode {
    private static int nextId = 0;

    protected static String getNextId() {
        return "node" + (nextId++);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExp(this);
    }
}
