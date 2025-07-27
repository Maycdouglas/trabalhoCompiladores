/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import java.util.Arrays;

public class ArrayValue implements Value {
    private final Value[] values;

    public ArrayValue(int size) {
        this.values = new Value[size];
    }

    public void set(int index, Value value) {
        if (index < 0 || index >= values.length) {
            throw new RuntimeException("Acesso a índice fora dos limites do array: " + index);
        }
        this.values[index] = value;
    }

    public Value get(int index) {
        if (index < 0 || index >= values.length) {
            throw new RuntimeException("Acesso a índice fora dos limites do array: " + index);
        }
        return this.values[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public Value[] getValues() {
        return this.values;
    }
}