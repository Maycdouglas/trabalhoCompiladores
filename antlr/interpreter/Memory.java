package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private final Map<String, Value> variables = new HashMap<>();

    public void assign(String name, Value value) {
        variables.put(name, value);
    }

    public Value lookup(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variável não declarada: " + name);
        }
        return variables.get(name);
    }
}
