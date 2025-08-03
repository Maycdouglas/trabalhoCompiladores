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

    private Type findVar(String varName) {
        for (int i = gamma.size() - 1; i >= 0; i--) {
            Map<String, Type> scope = gamma.get(i);
            if (scope.containsKey(varName)) {
                return scope.get(varName);
            }
        }
        throw new RuntimeException("Variável '" + varName + "' não declarada.");
    }

    // --- MÉTODOS DE VISITAÇÃO ---

    @Override
    public Type visitProg(Prog prog) {
        for (Def def : prog.definitions) {
            def.accept(this);
        }
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

    @Override
    public Type visitDef(Def def) {
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
    public Type visitCmdPrint(CmdPrint cmdPrint) {
        cmdPrint.value.accept(this);
        return null;
    }

    @Override
    public Type visitExp(Exp exp) {
        return exp.accept(this);
    }

    @Override
    public Type visitExpBinOp(ExpBinOp expBinOp) {
        Type leftType = expBinOp.left.accept(this);
        Type rightType = expBinOp.right.accept(this);

        if (!leftType.baseType.equals(rightType.baseType) || leftType.arrayDim != rightType.arrayDim) {
            throw new RuntimeException("Operação binária '" + expBinOp.op + "' entre tipos incompatíveis: "
                    + leftType.baseType + " e " + rightType.baseType);
        }

        switch (expBinOp.op) {
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
            case "==":
            case "!=":
            case "<":
                return new Type("Bool", 0);
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
        Type expType = expUnaryOp.exp.accept(this);

        switch (expUnaryOp.op) {
            case "!":
                if (!expType.baseType.equals("Bool")) {
                    throw new RuntimeException("Operador '!' só pode ser aplicado a booleanos.");
                }
                return new Type("Bool", 0);
            case "-":
                if (!expType.baseType.equals("Int") && !expType.baseType.equals("Float")) {
                    throw new RuntimeException("Operador '-' só pode ser aplicado a números.");
                }
                return expType;
            default:
                throw new RuntimeException("Operador unário desconhecido: " + expUnaryOp.op);
        }
    }

    @Override
    public Type visitExpVar(ExpVar expVar) {
        return findVar(expVar.name);
    }

    @Override
    public Type visitLValue(LValue lValue) {
        return lValue.accept(this);
    }

    @Override
    public Type visitLValueId(LValueId lValueId) {
        return findVar(lValueId.id);
    }

    // --- Métodos restantes da interface Visitor (necessários para compilar) ---

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
    public Type visitDecl(Decl decl) {
        return null;
    }

    @Override
    public Type visitExpBool(ExpBool exp) {
        return new Type("Bool", 0);
    }

    @Override
    public Type visitExpCall(ExpCall exp) {
        return null;
    }

    @Override
    public Type visitExpCallIndexed(ExpCallIndexed exp) {
        return null;
    }

    @Override
    public Type visitExpChar(ExpChar exp) {
        return new Type("Char", 0);
    }

    @Override
    public Type visitExpField(ExpField exp) {
        return null;
    }

    @Override
    public Type visitExpFloat(ExpFloat exp) {
        return new Type("Float", 0);
    }

    @Override
    public Type visitExpIndex(ExpIndex exp) {
        return null;
    }

    @Override
    public Type visitExpInt(ExpInt exp) {
        return new Type("Int", 0);
    }

    @Override
    public Type visitExpNew(ExpNew exp) {
        return null;
    }

    @Override
    public Type visitExpNull(ExpNull exp) {
        return new Type("Null", 0);
    }

    @Override
    public Type visitExpParen(ExpParen exp) {
        return exp.exp.accept(this);
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
    public Type visitLValueField(LValueField lValueField) {
        return null;
    }

    @Override
    public Type visitLValueIndex(LValueIndex lValueIndex) {
        return null;
    }

    @Override
    public Type visitParam(Param param) {
        return null;
    }

    @Override
    public Type visitType(Type type) {
        return null;
    }
}