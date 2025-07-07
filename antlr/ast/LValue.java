package ast;

import interpreter.Visitor;

public abstract class LValue extends Exp implements ASTNode {
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLValue(this);
    }
}
