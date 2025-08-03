/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package semant;

import ast.*;
import interpreter.Visitor;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SemanticVisitor implements Visitor<Type> {

    private final Map<String, Fun> theta = new HashMap<>(); // Para Funções
    private final Map<String, Data> delta = new HashMap<>(); // Para Tipos 'data'
    private final Stack<Map<String, Type>> gamma = new Stack<>(); // Para Variáveis (com âmbitos)

    public SemanticVisitor() {
        gamma.push(new HashMap<>());
    }

    private Map<String, Type> currentScope() {
        return gamma.peek();
    }

    @Override
    public Type visitProg(Prog prog) {
        for (Def def : prog.definitions) {
            def.accept(this);
        }

        // CORREÇÃO APLICADA: Esta parte agora irá de fato analisar os corpos das
        // funções.
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                checkFunBody((Fun) def);
            }
        }
        return null;
    }

    private void checkFunBody(Fun fun) {
        gamma.push(new HashMap<>());
        for (Param p : fun.params) {
            currentScope().put(p.id, p.type);
        }
        fun.body.accept(this);
        gamma.pop();
    }

    // ... (métodos visitDef, visitFun, visitData, etc., permanecem os mesmos) ...

    /**
     * NOVO: Implementação para analisar o comando 'print'.
     * A chave aqui é chamar o `accept` na expressão contida no 'print'.
     * Isso fará com que o visitor navegue até o nó da variável 'x'.
     */
    @Override
    public Type visitCmdPrint(CmdPrint cmdPrint) {
        cmdPrint.value.accept(this); // Visita a expressão dentro do print
        return null;
    }

    @Override
    public Type visitExpVar(ExpVar expVar) {
        // Agora que `visitCmdPrint` nos trouxe até aqui, este método será executado.
        Type varType = findVar(expVar.name); // Usando a função auxiliar
        if (varType == null) {
            throw new RuntimeException("Variável '" + expVar.name + "' não declarada neste âmbito.");
        }
        return varType;
    }

    /**
     * NOVO: Função auxiliar para procurar uma variável em todos os escopos,
     * do mais interno para o mais externo.
     */
    private Type findVar(String varName) {
        for (int i = gamma.size() - 1; i >= 0; i--) {
            Map<String, Type> scope = gamma.get(i);
            if (scope.containsKey(varName)) {
                return scope.get(varName);
            }
        }
        return null; // Retorna null se não encontrar em nenhum escopo
    }

    // --- O restante dos seus métodos de visitação ---
    // (Cole o restante do código que te enviei anteriormente aqui)
    // Cole o resto do seu código aqui
    @Override
    public Type visitDef(Def def) {
        // Delega para o tipo de definição específico (Fun, Data, etc.)
        return def.accept(this);
    }

    @Override
    public Type visitFun(Fun fun) {
        if (theta.containsKey(fun.id)) {
            throw new RuntimeException("Função '" + fun.id + "' já definida.");
        }
        theta.put(fun.id, fun);
        return null;
    }

    @Override
    public Type visitData(Data data) {
        return data.accept(this);
    }

    @Override
    public Type visitDataRegular(DataRegular dataRegular) {
        if (delta.containsKey(dataRegular.name)) {
            throw new RuntimeException("Tipo '" + dataRegular.name + "' já definido.");
        }
        delta.put(dataRegular.name, dataRegular);
        return null;
    }

    @Override
    public Type visitDataAbstract(DataAbstract dataAbstract) {
        if (delta.containsKey(dataAbstract.name)) {
            throw new RuntimeException("Tipo '" + dataAbstract.name + "' já definido.");
        }
        delta.put(dataAbstract.name, dataAbstract);
        return null;
    }

    @Override
    public Type visitCmd(Cmd cmd) {
        return cmd.accept(this);
    }

    @Override
    public Type visitCmdAssign(CmdAssign cmdAssign) {
        Type targetType = cmdAssign.target.accept(this);
        Type exprType = cmdAssign.expression.accept(this);

        if (!targetType.baseType.equals(exprType.baseType) || targetType.arrayDim != exprType.arrayDim) {
            throw new RuntimeException("Erro de tipo: não é possível atribuir o tipo " + exprType.baseType
                    + " a uma variável do tipo " + targetType.baseType);
        }
        return null;
    }

    @Override
    public Type visitCmdBlock(CmdBlock cmdBlock) {
        gamma.push(new HashMap<>());
        for (Cmd cmd : cmdBlock.cmds) {
            cmd.accept(this);
        }
        gamma.pop();
        return null;
    }

    @Override
    public Type visitExp(Exp exp) {
        return exp.accept(this);
    }

    @Override
    public Type visitLValue(LValue lValue) {
        return lValue.accept(this);
    }

    @Override
    public Type visitLValueId(LValueId lValueId) {
        return findVar(lValueId.id);
    }

    // ... (início do arquivo e métodos anteriores) ...

    /**
     * NOVO: Implementação da verificação de tipos para expressões binárias.
     * 1. Visita recursivamente os operandos da esquerda e da direita para obter
     * seus tipos.
     * 2. Verifica se os tipos são compatíveis para o operador em questão.
     * 3. Retorna o tipo resultante da expressão.
     */
    @Override
    public Type visitExpBinOp(ExpBinOp expBinOp) {
        Type leftType = expBinOp.left.accept(this);
        Type rightType = expBinOp.right.accept(this);

        // Verifica se os tipos são iguais (simplificação inicial)
        if (!leftType.baseType.equals(rightType.baseType) || leftType.arrayDim != rightType.arrayDim) {
            throw new RuntimeException("Operação binária '" + expBinOp.op + "' entre tipos incompatíveis: " +
                    leftType.baseType + " e " + rightType.baseType);
        }

        switch (expBinOp.op) {
            // Operadores Aritméticos: retornam o mesmo tipo dos operandos (Int ou Float)
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                if (!leftType.baseType.equals("Int") && !leftType.baseType.equals("Float")) {
                    throw new RuntimeException(
                            "Operador aritmético '" + expBinOp.op + "' só pode ser usado com Int ou Float.");
                }
                return leftType;

            // Operadores Relacionais e de Igualdade: sempre retornam Bool
            case "==":
            case "!=":
            case "<":
                return new Type("Bool", 0);

            // Operadores Lógicos: exigem operandos Bool e retornam Bool
            case "&&":
                if (!leftType.baseType.equals("Bool")) {
                    throw new RuntimeException("Operador lógico '&&' requer operandos do tipo Bool.");
                }
                return new Type("Bool", 0);

            default:
                throw new RuntimeException("Operador binário desconhecido: " + expBinOp.op);
        }
    }

    @Override
    public Type visitExpUnaryOp(ExpUnaryOp expUnaryOp) {
        return null;
    }

    @Override
    public Type visitExpInt(ExpInt exp) {
        return new Type("Int", 0);
    }

    @Override
    public Type visitExpFloat(ExpFloat expFloat) {
        return new Type("Float", 0);
    }

    @Override
    public Type visitExpBool(ExpBool expBool) {
        return new Type("Bool", 0);
    }

    @Override
    public Type visitExpChar(ExpChar expChar) {
        return new Type("Char", 0);
    }

    @Override
    public Type visitExpNull(ExpNull expNull) {
        return new Type("Null", 0);
    }

    @Override
    public Type visitCmdCall(CmdCall cmdCall) {
        return null;
    }

    @Override
    public Type visitCmdIf(CmdIf cmdIf) {
        return null;
    }

    @Override
    public Type visitCmdIterate(CmdIterate cmdIterate) {
        return null;
    }

    @Override
    public Type visitCmdRead(CmdRead cmdRead) {
        return null;
    }

    @Override
    public Type visitCmdReturn(CmdReturn cmdReturn) {
        return null;
    }

    @Override
    public Type visitExpCall(ExpCall expCall) {
        return null;
    }

    @Override
    public Type visitExpCallIndexed(ExpCallIndexed expCallIndexed) {
        return null;
    }

    @Override
    public Type visitExpParen(ExpParen expParen) {
        return expParen.exp.accept(this);
    }

    @Override
    public Type visitExpNew(ExpNew expNew) {
        return null;
    }

    @Override
    public Type visitExpIndex(ExpIndex expIndex) {
        return null;
    }

    @Override
    public Type visitExpField(ExpField expField) {
        return null;
    }

    @Override
    public Type visitLValueField(LValueField lValueField) {
        return null;
    }

    @Override
    public Type visitLValueIndex(LValueIndex lValueIndex) {
        return null;
    }

    @Override
    public Type visitItCond(ItCond itCond) {
        return itCond.accept(this);
    }

    @Override
    public Type visitItCondExpr(ItCondExpr itCondExpr) {
        return null;
    }

    @Override
    public Type visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return null;
    }

    @Override
    public Type visitDecl(Decl decl) {
        return null;
    }

    @Override
    public Type visitParam(Param param) {
        return null;
    }

    @Override
    public Type visitType(ast.Type type) {
        return null;
    }

}