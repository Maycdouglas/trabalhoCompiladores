package ast;

import interpreter.Visitor;

public interface ASTNode {
    <T> T accept(Visitor<T> visitor);
}
