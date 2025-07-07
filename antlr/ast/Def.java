package ast;

import interpreter.Visitor;

// Interface Def: para ser implementada por Data e Fun
public interface Def extends ASTNode {

    @Override
    public <T> T accept(Visitor<T> visitor);
}