/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package semant;

import ast.*;
import interpreter.Visitor; // A sua classe Visitor está no pacote interpreter
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SemanticVisitor implements Visitor<Type> {

    private final Map<String, Fun> theta = new HashMap<>(); // Para Funções
    private final Map<String, Data> delta = new HashMap<>(); // Para Tipos 'data'
    private final Stack<Map<String, Type>> gamma = new Stack<>(); // Para Variáveis (com âmbitos)

    public SemanticVisitor() {
        // Inicializa o âmbito global
        gamma.push(new HashMap<>());
    }

    private Map<String, Type> currentScope() {
        return gamma.peek();
    }

    // --- MÉTODOS DE NÓS ESTRUTURAIS (TOPO DA ÁRVORE) ---

    @Override
    public Type visitProg(Prog prog) {
        // --- FASE 1: REGISTO DE DEFINIÇÕES GLOBAIS ---
        for (Def def : prog.definitions) {
            def.accept(this);
        }

        // --- FASE 2: VERIFICAÇÃO DOS CORPOS DAS FUNÇÕES ---
        // (A lógica de verificação detalhada será adicionada aqui)
        for (Def def : prog.definitions) {
            if (def instanceof Fun) {
                // TODO: Chamar um método para verificar o corpo da função
                // ex: checkFunBody((Fun) def);
            }
        }
        return null;
    }

    @Override
    public Type visitDef(Def def) {
        // Delega para o tipo de definição específico (Fun, Data, etc.)
        return def.accept(this);
    }

    // --- MÉTODOS DE DEFINIÇÕES (PRIMEIRA PASSAGEM) ---

    @Override
    public Type visitFun(Fun fun) {
        if (theta.containsKey(fun.id)) {
            // TODO: Lançar erro de redefinição de função
        }
        theta.put(fun.id, fun);
        return null; // A definição em si não tem um tipo
    }

    @Override
    public Type visitData(Data data) {
        // Delega para o tipo específico de Data
        return data.accept(this);
    }

    @Override
    public Type visitDataRegular(DataRegular dataRegular) {
        if (delta.containsKey(dataRegular.name)) {
            // TODO: Lançar erro de redefinição de tipo
        }
        delta.put(dataRegular.name, dataRegular);
        return null; // A definição não tem um tipo
    }

    @Override
    public Type visitDataAbstract(DataAbstract dataAbstract) {
        // TODO Auto-generated method stub
        return null;
    }

    // --- MÉTODOS DE COMANDOS (SEGUNDA PASSAGEM) ---

    @Override
    public Type visitCmd(Cmd cmd) {
        return cmd.accept(this);
    }

    @Override
    public Type visitCmdAssign(CmdAssign cmdAssign) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdBlock(CmdBlock cmdBlock) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdCall(CmdCall cmdCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdIf(CmdIf cmdIf) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdIterate(CmdIterate cmdIterate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdPrint(CmdPrint cmdPrint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdRead(CmdRead cmdRead) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitCmdReturn(CmdReturn cmdReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    // --- MÉTODOS DE EXPRESSÕES (SEGUNDA PASSAGEM) ---

    @Override
    public Type visitExp(Exp exp) {
        return exp.accept(this);
    }

    @Override
    public Type visitExpBinOp(ExpBinOp expBinOp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitExpUnaryOp(ExpUnaryOp expUnaryOp) {
        // TODO Auto-generated method stub
        return null;
    }

    // -- Expressões Literais --

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
        // Null é especial, pode não ter um tipo fixo, mas podemos representá-lo
        return new Type("Null", 0); // Pode precisar de um tipo especial
    }

    @Override
    public Type visitExpVar(ExpVar expVar) {
        // Procura a variável no âmbito atual (tabela gamma)
        Type varType = currentScope().get(expVar.name);

        if (varType == null) {
            // Se não encontrou, lança uma exceção com uma mensagem clara.
            throw new RuntimeException("Variável '" + expVar.name + "' não declarada neste âmbito.");
        }

        // Se encontrou, retorna o tipo da variável para outras verificações.
        return varType;
    }

    @Override
    public Type visitExpCall(ExpCall expCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitExpCallIndexed(ExpCallIndexed expCallIndexed) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitExpParen(ExpParen expParen) {
        // O tipo de uma expressão entre parêntesis é o tipo da expressão interna.
        return expParen.exp.accept(this);
    }

    @Override
    public Type visitExpNew(ExpNew expNew) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitExpIndex(ExpIndex expIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitExpField(ExpField expField) {
        // TODO Auto-generated method stub
        return null;
    }

    // --- MÉTODOS DE LVALUES E CONDIÇÕES ---

    @Override
    public Type visitLValue(LValue lValue) {
        return lValue.accept(this);
    }

    @Override
    public Type visitLValueId(LValueId lValueId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitLValueField(LValueField lValueField) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitLValueIndex(LValueIndex lValueIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitItCond(ItCond itCond) {
        return itCond.accept(this);
    }

    @Override
    public Type visitItCondExpr(ItCondExpr itCondExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitItCondLabelled(ItCondLabelled itCondLabelled) {
        // TODO Auto-generated method stub
        return null;
    }

    // --- MÉTODOS DE DECLARAÇÕES E TIPOS ---

    @Override
    public Type visitDecl(Decl decl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitParam(Param param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visitType(Type type) {
        // TODO Auto-generated method stub
        return null;
    }
}