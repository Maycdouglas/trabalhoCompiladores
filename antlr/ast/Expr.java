package ast;

import java.util.HashMap;

/*
 * Esta classe representa um comando de Impressão.
 * Expr
 */
public abstract class Expr extends Node {

    public Expr(int l, int c) {
        super(l, c);
    }

    @Override
    public int interpret(HashMap<String, Integer> mem) {
        throw new UnsupportedOperationException("AbstractFun not yet implemented");
    }

}
