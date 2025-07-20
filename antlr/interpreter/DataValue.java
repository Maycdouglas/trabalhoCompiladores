/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import java.util.HashMap;
import java.util.Map;
import ast.Data;

public class DataValue implements Value {

    private final Data definition;
    private final Map<String, Value> fields = new HashMap<>();

    public DataValue(Data definition) {
        this.definition = definition;
    }

    public void setField(String fieldName, Value value) {
        fields.put(fieldName, value);
    }

    public Value getField(String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new RuntimeException("Campo '" + fieldName + "' não inicializado na instância.");
        }
        return fields.get(fieldName);
    }

    @Override
    public String toString() {
        return definition.name + fields.toString();
    }
}