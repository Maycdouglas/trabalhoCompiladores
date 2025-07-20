/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

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

    public ArrayValue newArray(int size) {
        return new ArrayValue(size);
    }

    public void assignToArray(String name, int index, Value value) {
        Value val = lookup(name);
        if (val instanceof ArrayValue) {
            ((ArrayValue) val).set(index, value);
        } else {
            throw new RuntimeException("A variável '" + name + "' não é um array.");
        }
    }

    public Value lookupInArray(String name, int index) {
        Value val = lookup(name);
        if (val instanceof ArrayValue) {
            return ((ArrayValue) val).get(index);
        } else {
            throw new RuntimeException("A variável '" + name + "' não é um array.");
        }
    }
}
