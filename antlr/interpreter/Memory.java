/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import ast.*;
import java.util.*;

/**
 * A classe Memory gerencia o estado de execução do interpretador.
 * Ela é responsável por três áreas principais:
 * 1.  Armazenar os valores das variáveis e controlar o escopo (global, funções).
 * 2.  Manter um registro de todas as definições de funções (tabela de símbolos de funções).
 * 3.  Manter um registro de todas as definições de tipos de dados (data).
 */
public class Memory {

    private final Stack<Map<String, Value>> memoryStack = new Stack<>(); // A pilha de memória, onde cada elemento da pilha é um mapa que representa um escopo
    private final Map<String, Fun> functionDefinitions = new HashMap<>();  // Tabela de símbolos para as funções
    private final Map<String, Data> dataDefinitions = new HashMap<>(); // Tabela de símbolos para os tipos de dados

    public Memory() {
        // O primeiro escopo na pilha é sempre o escopo global.
        memoryStack.push(new HashMap<>());
    }

    // ----------------------
    // Gerenciamento de Escopos
    // ----------------------

    /**
     * Cria e empilha um novo escopo vazio.
     * Normalmente chamado ao entrar em um bloco de comandos (como if, iterate) que cria um novo escopo.
     */
    public void pushScope() {
        memoryStack.push(new HashMap<>());
    }

    /**
     * Sobrecarga de pushScope que cria e empilha um novo escopo,
     * inicializando-o com um conjunto de variáveis.
     * Usado principalmente na chamada de funções para criar um escopo com os parâmetros.
     * @param initialVars Um mapa de nomes de variáveis e seus valores iniciais.
     */
    public void pushScope(Map<String, Value> initialVars) {
        memoryStack.push(new HashMap<>(initialVars));
    }

    // Remove (desempilha) o escopo atual (mais interno)
    public void popScope() {
        if (memoryStack.size() > 1) {
            memoryStack.pop();
        } else {
            throw new IllegalStateException("Não é possível remover o escopo global.");
        }
    }
    // Retorna o map que representa o escopo atual
    public Map<String, Value> currentScope() {
        return memoryStack.peek();
    }

    /**
     * Procura por uma variável em todos os escopos, do mais interno para o mais externo.
     * Esta é a operação de "lookup" que respeita o sombreamento de variáveis.
     * @param name O nome da variável a ser procurada.
     * @return O Value associado à variável, se encontrado.
     * @throws RuntimeException se a variável não for encontrada em nenhum escopo.
     */
    public Value lookup(String name) {
        for (int i = memoryStack.size() - 1; i >= 0; i--) {
            Map<String, Value> scope = memoryStack.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variável não declarada: " + name);
    }

    // Define ou atualiza uma variável no escopo ATUAL (o mais interno)
    public void setVar(String name, Value value) {
        currentScope().put(name, value);
    }

    // ----------------------
    // Acesso a Funções
    // ----------------------
    
    // Armazena a definição de uma função na tabela de símbolos de funções
    public void setFunction(String name, Fun function) {
        functionDefinitions.put(name, function);
    }

    // Recupera a definição de uma função pelo nome
    public Fun getFunction(String name) {
        Fun f = functionDefinitions.get(name);
        if (f == null) {
            throw new RuntimeException("Função não definida: " + name);
        }
        return f;
    }

    // ----------------------
    // Acesso a Definições de Tipos (Data)
    // ----------------------

    // Armazena a definição de um tipo 'data' na tabela de símbolos de tipos
    public void setDataDef(String name, Data dataDef) {
        dataDefinitions.put(name, dataDef);
    }

    // Recupera a definição de um tipo 'data' pelo nome.
    public Data getDataDef(String name) {
        Data d = dataDefinitions.get(name);
        if (d == null) {
            throw new RuntimeException("Data definition não encontrada: " + name);
        }
        return d;
    }

    // Verifica se uma definição de 'data' com o nome especificado existe.
    public boolean hasDataDef(String name) {
        return dataDefinitions.containsKey(name);
    }
}
