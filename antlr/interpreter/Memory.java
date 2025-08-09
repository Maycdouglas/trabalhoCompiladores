/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import ast.*;
import java.util.*;

public class Memory {

    private final Stack<Map<String, Value>> memoryStack = new Stack<>();
    private final Map<String, Fun> functionDefinitions = new HashMap<>();
    private final Map<String, Data> dataDefinitions = new HashMap<>();

    public Memory() {
        // Escopo global
        memoryStack.push(new HashMap<>());
    }

    // ----------------------
    // Escopos
    // ----------------------
    public void pushScope() {
        memoryStack.push(new HashMap<>());
    }

    // Sobrecarga, para inicializar o escopo com variáveis
    public void pushScope(Map<String, Value> initialVars) {
        memoryStack.push(new HashMap<>(initialVars));
    }

    public void popScope() {
        if (memoryStack.size() > 1) {
            memoryStack.pop();
        } else {
            throw new IllegalStateException("Não é possível remover o escopo global.");
        }
    }

    public Map<String, Value> currentScope() {
        return memoryStack.peek();
    }

    // Busca a variável pelo nome nos escopos, do topo para baixo
    public Value lookup(String name) {
        for (int i = memoryStack.size() - 1; i >= 0; i--) {
            Map<String, Value> scope = memoryStack.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variável não declarada: " + name);
    }

    // ----------------------
    // Variáveis
    // ----------------------
    public void setVar(String name, Value value) {
        currentScope().put(name, value);
    }

    public Value getVar(String name) {
        // Procura do escopo atual para o global
        for (int i = memoryStack.size() - 1; i >= 0; i--) {
            Map<String, Value> scope = memoryStack.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variável não definida: " + name);
    }

    // ----------------------
    // Funções
    // ----------------------
    public void setFunction(String name, Fun function) {
        functionDefinitions.put(name, function);
    }

    public Fun getFunction(String name) {
        Fun f = functionDefinitions.get(name);
        if (f == null) {
            throw new RuntimeException("Função não definida: " + name);
        }
        return f;
    }

    // ----------------------
    // Data Definitions
    // ----------------------
    public void setDataDef(String name, Data dataDef) {
        dataDefinitions.put(name, dataDef);
    }

    public Data getDataDef(String name) {
        Data d = dataDefinitions.get(name);
        if (d == null) {
            throw new RuntimeException("Data definition não encontrada: " + name);
        }
        return d;
    }

    public boolean hasDataDef(String name) {
        return dataDefinitions.containsKey(name);
    }
}
