// TypeName.java
package ast;

import java.util.HashMap;

public class TypeName extends Type {
    private String name;

    public TypeName(int line, int col, String name) {
        super(line, col);
        this.name = name;
    }

    @Override
    public Object interpret(HashMap<String, Object> mem) {
        // Lógica de interpretação para tipos
        return null; // Ou implementação apropriada
    }
}