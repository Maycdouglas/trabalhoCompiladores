package ast;

import interpreter.Visitor;

// Comando base (futuramente vai herdar para os comandos específicos)
public abstract class Cmd implements ASTNode {

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCmd(this);
    }
}