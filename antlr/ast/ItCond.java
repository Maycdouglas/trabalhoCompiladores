package ast;

import interpreter.Visitor;

// Condição de iteração base (para 'iterate')
public abstract class ItCond implements DotPrintable, ASTNode  {
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitItCond(this);
    }
}
