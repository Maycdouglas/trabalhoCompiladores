// TypeName.java
package ast;

import java.util.HashMap;

public class TypeName extends Type {
    private String name;

    public TypeName(int line, int col, String name) {
        super(line, col);
        this.name = name;
    }

}