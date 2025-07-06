package ast;

public abstract class Exp extends Cmd implements DotPrintable {
    private static int nextId = 0;

    protected static String getNextId() {
        return "node" + (nextId++);
    }
}
