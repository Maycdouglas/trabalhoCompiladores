/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package semant;

import ast.*;
import interpreter.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SemanticVisitor implements Visitor<Type> {

    private final Stack<Map<String, Type>> gamma = new Stack<>();
    private final Map<String, Fun> theta = new HashMap<>();
    private final Map<String, Data> delta = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    private Fun currentFunction = null;

    public SemanticVisitor() {
        gamma.push(new HashMap<>());
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void printErrors() {
        for (String error : errors) {
            System.err.println(error);
        }
    }

    private void addError(int line, String message) {
        errors.add("Erro Semântico (linha " + line + "): " + message);
    }

    private Map<String, Type> currentScope() {
        return gamma.peek();
    }

    public Map<String, Data> getDelta() {
        return this.delta;
    }

    public Map<String, Fun> getTheta() {
        return this.theta;
    }

    private Type findVar(String varName) {
        for (int i = gamma.size() - 1; i >= 0; i--) {
            Map<String, Type> scope = gamma.get(i);
            if (scope.containsKey(varName)) {
                return scope.get(varName);
            }
        }
        return null;
    }

    @Override
    public Type visitCmdAssign(CmdAssign cmd) {
        Type expressionType = cmd.expression.accept(this);

        if (expressionType.isError()) {
            return Type.ERROR;
        }

        if (cmd.target instanceof LValueId) {
            LValueId lvalueId = (LValueId) cmd.target;
            String varName = lvalueId.id;

            Type lValueType = findVar(varName);

            if (lValueType == null) {
                currentScope().put(varName, expressionType);
                lvalueId.expType = expressionType;
                return expressionType;
            } else {
                lvalueId.expType = lValueType;
                boolean compatible = lValueType.isEquivalent(expressionType) ||
                        (lValueType.isReference() && expressionType.isNull());
                if (!compatible) {
                    addError(cmd.getLine(),
                            "Tipos incompatíveis na atribuição. A variável '" + varName +
                                    "' é do tipo '" + lValueType + "' mas recebeu '" + expressionType + "'.");
                    return Type.ERROR;
                }
                return lValueType;
            }
        } else {
            Type lValueType = cmd.target.accept(this);

            if (lValueType.isError()) {
                return Type.ERROR;
            }

            boolean compatible = lValueType.isEquivalent(expressionType) ||
                    (lValueType.isReference() && expressionType.isNull());
            if (!compatible) {
                addError(cmd.getLine(),
                        "Tipos incompatíveis na atribuição. O alvo é do tipo '" +
                                lValueType + "' mas recebeu '" + expressionType + "'.");
                return Type.ERROR;
            }
            return lValueType;
        }
    }

    @Override
    public Type visitCmdBlock(CmdBlock cmd) {
        gamma.push(new HashMap<>());
        for (Cmd commandInBlock : cmd.cmds) {
            commandInBlock.accept(this);
        }
        gamma.pop();
        return null;
    }

    @Override
    public Type visitCmdCall(CmdCall cmd) {
        Fun funDef = theta.get(cmd.id);
        if (funDef == null) {
            addError(cmd.getLine(), "Função '" + cmd.id + "' não declarada.");
            return null;
        }

        if (funDef.params.size() != cmd.args.size()) {
            addError(cmd.getLine(), "Número incorreto de argumentos para a função '"
                    + cmd.id + "'. Esperava " +
                    funDef.params.size() + ", mas recebeu " + cmd.args.size() + ".");
        } else {
            for (int i = 0; i < funDef.params.size(); i++) {
                Type argType = cmd.args.get(i).accept(this);
                Type paramType = funDef.params.get(i).type;
                if (!argType.isEquivalent(paramType)) {
                    addError(cmd.args.get(i).getLine(),
                            "Tipo incorreto para o argumento " + (i + 1) + " na chamada da função '" + cmd.id + "'.");
                }
            }
        }

        if (cmd.rets != null && !cmd.rets.isEmpty()) {
            if (cmd.rets.size() != funDef.retTypes.size()) {
                addError(cmd.getLine(), "Número de variáveis de retorno (" + cmd.rets.size() +
                        ") é diferente do número de valores retornados (" + funDef.retTypes.size() +
                        ") pela função '" + cmd.id + "'.");
            } else {
                for (int i = 0; i < cmd.rets.size(); i++) {
                    LValue targetLval = cmd.rets.get(i);
                    Type returnType = funDef.retTypes.get(i);

                    if (targetLval instanceof LValueId) {
                        String varName = ((LValueId) targetLval).id;
                        Type existingVarType = findVar(varName);

                        if (existingVarType == null) {
                            currentScope().put(varName, returnType);
                            ((LValueId) targetLval).expType = returnType;
                        } else {
                            ((LValueId) targetLval).expType = existingVarType;
                            if (!existingVarType.isEquivalent(returnType)) {
                                addError(targetLval.getLine(),
                                        "Incompatibilidade de tipo ao atribuir o retorno da função '" + cmd.id +
                                                "' à variável '" + varName + "'. Esperado '" + existingVarType
                                                + "' mas o retorno é '" + returnType + "'.");
                            }
                        }
                    } else {
                        Type lvalType = targetLval.accept(this);
                        if (!lvalType.isError() && !lvalType.isEquivalent(returnType)) {
                            addError(targetLval.getLine(),
                                    "Incompatibilidade de tipo ao atribuir o retorno da função '" + cmd.id +
                                            "'. O alvo é do tipo '" + lvalType + "' mas o retorno é '" + returnType
                                            + "'.");
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Type visitCmdIf(CmdIf cmd) {
        Type condType = cmd.condition.accept(this);
        if (!condType.baseType.equals("Bool")) {
            throw new RuntimeException("Linha " + cmd.getLine() + ": A condição de um 'if' deve ser do tipo Bool.");
        }

        cmd.thenBranch.accept(this);
        if (cmd.elseBranch != null) {
            cmd.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public Type visitCmdIterate(CmdIterate cmd) {
        Type condType = cmd.condition.accept(this);

        if (cmd.condition instanceof ItCondLabelled) {
            ItCondLabelled itCond = (ItCondLabelled) cmd.condition;
            Type loopVarType;

            if (condType.arrayDim > 0) {
                loopVarType = new Type(condType.baseType, condType.arrayDim - 1);
            } else if (condType.baseType.equals("Int")) {
                loopVarType = new Type("Int", 0);
            } else {
                throw new RuntimeException("Linha " + cmd.getLine() + ": " +
                        "A expressão em um laço 'iterate' rotulado deve ser um inteiro ou um array.");
            }

            gamma.push(new HashMap<>());
            currentScope().put(itCond.label, loopVarType);
            cmd.body.accept(this);
            gamma.pop();

        } else if (cmd.condition instanceof ItCondExpr) {
            if (condType.baseType.equals("Int") && condType.arrayDim == 0) {
                cmd.body.accept(this);
            } else if (condType.baseType.equals("Bool") && condType.arrayDim == 0) {
                cmd.body.accept(this);
            } else {
                throw new RuntimeException("Linha " + cmd.getLine()
                        + ": A condição de um laço 'iterate' sem rótulo deve ser do tipo Int ou Bool.");
            }
        }
        return null;
    }

    @Override
    public Type visitCmdPrint(CmdPrint cmd) {
        cmd.value.accept(this);
        return null;
    }

    @Override
    public Type visitCmdRead(CmdRead cmd) {
        cmd.lvalue.accept(this);
        return null;
    }

    @Override
    public Type visitCmdReturn(CmdReturn cmd) {
        if (currentFunction == null) {
            addError(cmd.getLine(), "Comando 'return' só pode ser usado dentro de uma função.");
            return Type.ERROR;
        }

        List<Type> declaredReturnTypes = currentFunction.retTypes;
        List<Exp> returnedExpressions = cmd.values;

        if (declaredReturnTypes.size() != returnedExpressions.size()) {
            addError(cmd.getLine(), "A função '" + currentFunction.id + "' espera " + declaredReturnTypes.size() +
                    " valores de retorno, mas " + returnedExpressions.size() + " foram fornecidos.");
            for (Exp exp : returnedExpressions) {
                exp.accept(this);
            }
            return Type.ERROR;
        }

        for (int i = 0; i < declaredReturnTypes.size(); i++) {
            Type declaredType = declaredReturnTypes.get(i);
            Type actualType = returnedExpressions.get(i).accept(this);

            if (!actualType.isError() && !declaredType.isEquivalent(actualType)) {
                addError(returnedExpressions.get(i).getLine(),
                        "Tipo de retorno incompatível na posição " + i + ". Esperado '" + declaredType +
                                "', mas encontrado '" + actualType + "'.");
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visitDef(Def def) {
        return def.accept(this);
    }

    @Override
    public Type visitFun(Fun fun) {
        if (theta.containsKey(fun.id)) {
            throw new RuntimeException("Linha " + fun.getLine() + ": Função '" + fun.id + "' já definida.");
        }
        theta.put(fun.id, fun);
        return null;
    }

    @Override
    public Type visitData(Data data) {
        return data.accept(this);
    }

    @Override
    public Type visitDataRegular(DataRegular data) {
        if (delta.containsKey(data.name)) {
            throw new RuntimeException("Linha " + data.getLine() + ": Tipo '" + data.name + "' já definido.");
        }

        Map<String, Type> fields = new HashMap<>();
        for (Decl decl : data.declarations) {
            if (fields.containsKey(decl.id)) {
                throw new RuntimeException("Linha " + decl.getLine() + ": Campo '" + decl.id
                        + "' já foi declarado no tipo '" + data.name + "'.");
            }
            fields.put(decl.id, decl.type);
        }

        delta.put(data.name, data);
        return null;
    }

    @Override
    public Type visitDataAbstract(DataAbstract data) {
        if (delta.containsKey(data.name)) {
            throw new RuntimeException("Linha " + data.getLine() + ": Tipo '" + data.name + "' já definido.");
        }

        Map<String, Type> fields = new HashMap<>();
        for (Decl decl : data.declarations) {
            if (fields.containsKey(decl.id)) {
                throw new RuntimeException("Linha " + decl.getLine() + ": Campo '" + decl.id
                        + "' já foi declarado no tipo '" + data.name + "'.");
            }
            fields.put(decl.id, decl.type);
        }

        delta.put(data.name, data);
        return null;
    }

    @Override
    public Type visitExpBinOp(ExpBinOp expBinOp) {
        Type leftType = expBinOp.left.accept(this);
        Type rightType = expBinOp.right.accept(this);
        Type resultType = null;

        if (leftType.isError() || rightType.isError()) {
            expBinOp.expType = Type.ERROR;
            return Type.ERROR;
        }

        switch (expBinOp.op) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                if (leftType.isNumeric() && rightType.isNumeric()) {
                    if (leftType.isFloat() || rightType.isFloat()) {
                        resultType = new Type("Float", 0);
                    } else {
                        resultType = new Type("Int", 0);
                    }
                }
                break;

            case "<":
                if (leftType.isNumeric() && rightType.isNumeric() && leftType.isEquivalent(rightType)) {
                    resultType = new Type("Bool", 0);
                }
                break;

            case "==":
            case "!=":
                if (leftType.isPrimitive() && rightType.isPrimitive() && leftType.isEquivalent(rightType)) {
                    resultType = new Type("Bool", 0);
                } else if (leftType.isReference() && rightType.isNull()) {
                    resultType = new Type("Bool", 0);
                } else if (rightType.isReference() && leftType.isNull()) {
                    resultType = new Type("Bool", 0);
                } else if (leftType.isReference() && rightType.isReference() && leftType.isEquivalent(rightType)) {
                    resultType = new Type("Bool", 0);
                }
                break;

            case "&&":
                if (leftType.isBool() && rightType.isBool()) {
                    resultType = new Type("Bool", 0);
                }
                break;
        }

        if (resultType == null) {
            addError(expBinOp.getLine(), "Operação '" + expBinOp.op + "' inválida entre os tipos "
                    + leftType.toString() + " e " + rightType.toString());
            expBinOp.expType = Type.ERROR;
            return Type.ERROR;
        }

        expBinOp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitExpUnaryOp(ExpUnaryOp expUnaryOp) {
        Type expType = expUnaryOp.exp.accept(this);
        Type resultType = null;

        switch (expUnaryOp.op) {
            case "!":
                if (!expType.baseType.equals("Bool")) {
                    throw new RuntimeException(
                            "Linha " + expUnaryOp.getLine() + ": Operador '!' só pode ser aplicado a booleanos.");
                }
                resultType = new Type("Bool", 0);
                break;
            case "-":
                if (!expType.baseType.equals("Int") && !expType.baseType.equals("Float")) {
                    throw new RuntimeException(
                            "Linha " + expUnaryOp.getLine() + ": Operador '-' só pode ser aplicado a números.");
                }
                resultType = expType;
                break;
            default:
                throw new RuntimeException(
                        "Linha " + expUnaryOp.getLine() + ": Operador unário desconhecido: " + expUnaryOp.op);
        }

        expUnaryOp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitExpVar(ExpVar expVar) {
        Type varType = findVar(expVar.name);

        if (varType == null) {
            addError(expVar.getLine(), "Variável '" + expVar.name + "' não declarada.");
            expVar.expType = Type.ERROR;
            return Type.ERROR;
        }

        expVar.expType = varType;
        return varType;
    }

    @Override
    public Type visitLValueId(LValueId lValueId) {
        Type varType = findVar(lValueId.id);

        if (varType == null) {
            addError(lValueId.getLine(), "Variável '" + lValueId.id + "' não declarada.");
            lValueId.expType = Type.ERROR;
            return Type.ERROR;
        }

        lValueId.expType = varType;
        return varType;
    }

    @Override
    public Type visitExpInt(ExpInt exp) {
        Type intType = new Type("Int", 0);
        exp.expType = intType;
        return intType;
    }

    @Override
    public Type visitExpFloat(ExpFloat exp) {
        Type floatType = new Type("Float", 0);
        exp.expType = floatType;
        return floatType;
    }

    @Override
    public Type visitExpChar(ExpChar exp) {
        Type charType = new Type("Char", 0);
        exp.expType = charType;
        return charType;
    }

    @Override
    public Type visitExpBool(ExpBool exp) {
        Type boolType = new Type("Bool", 0);
        exp.expType = boolType;
        return boolType;
    }

    @Override
    public Type visitExpNull(ExpNull exp) {
        Type nullType = new Type("Null", 0);
        exp.expType = nullType;
        return nullType;
    }

    @Override
    public Type visitExpParen(ExpParen exp) {
        return exp.exp.accept(this);
    }

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

        if (!theta.containsKey("main")) {
            addError(0, "Função 'main' não definida no programa.");
        }

        return null;
    }

    private void checkFunBody(Fun fun) {
        this.currentFunction = fun;
        gamma.push(new HashMap<>());
        for (Param p : fun.params) {
            currentScope().put(p.id, p.type);
        }
        fun.body.accept(this);
        gamma.pop();
        this.currentFunction = null;
    }

    @Override
    public Type visitLValue(LValue lValue) {
        return lValue.accept(this);
    }

    @Override
    public Type visitCmd(Cmd cmd) {
        return cmd.accept(this);
    }

    @Override
    public Type visitDecl(Decl decl) {
        return null;
    }

    @Override
    public Type visitExpCall(ExpCall exp) {
        Fun funDef = theta.get(exp.id);
        if (funDef == null) {
            throw new RuntimeException("Linha " + exp.getLine() + ": Função '" + exp.id + "' não definida.");
        }

        if (funDef.params.size() != exp.args.size()) {
            throw new RuntimeException(
                    "Linha " + exp.getLine() + ": Número incorreto de argumentos para a função '" + exp.id + "'.");
        }

        if (funDef.retTypes.size() > 1) {
            throw new RuntimeException("Linha " + exp.getLine() + ": Chamada de função com múltiplos retornos '"
                    + exp.id + "' deve ser indexada.");
        }
        if (funDef.retTypes.isEmpty()) {
            addError(exp.line,
                    "Função '" + exp.id + "' não possui valor de retorno e não pode ser usada em uma expressão.");
            exp.expType = Type.ERROR;
        } else if (funDef.retTypes.size() == 1) {
            exp.expType = funDef.retTypes.get(0);
        } else {
            exp.expType = Type.VOID;
        }
        return exp.expType;
    }

    @Override
    public Type visitExpCallIndexed(ExpCallIndexed exp) {
        Fun funDef = theta.get(exp.call.id);
        if (funDef == null) {
            throw new RuntimeException("Linha " + exp.getLine() + ": Função '" + exp.call.id + "' não definida.");
        }

        if (funDef.params.size() != exp.call.args.size()) {
            throw new RuntimeException(
                    "Linha " + exp.getLine() + ": Número incorreto de argumentos para a função '" + exp.call.id + "'.");
        }
        for (int i = 0; i < funDef.params.size(); i++) {
            Type argType = exp.call.args.get(i).accept(this);
            Type paramType = funDef.params.get(i).type;
            if (!argType.baseType.equals(paramType.baseType) || argType.arrayDim != paramType.arrayDim) {
                throw new RuntimeException("Linha " + exp.getLine() + ": " +
                        "Tipo incorreto para o argumento " + (i + 1) + " na chamada da função '" + exp.call.id + "'.");
            }
        }

        Type indexType = exp.index.accept(this);
        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException(
                    "Linha " + exp.getLine() + ": O índice de um retorno de função deve ser do tipo Int.");
        }

        Type resultType = null;

        if (exp.index instanceof ExpInt) {
            int idx = ((ExpInt) exp.index).value;
            if (idx < 0 || idx >= funDef.retTypes.size()) {
                throw new RuntimeException("Linha " + exp.getLine() + ": " +
                        "Índice de retorno " + idx + " fora dos limites para a função '" + funDef.id + "'.");
            }
            resultType = funDef.retTypes.get(idx);
        } else {
            if (funDef.retTypes.size() == 1) {
                resultType = funDef.retTypes.get(0);
            } else {
                throw new RuntimeException("Linha " + exp.getLine() + ": " +
                        "Não é possível determinar estaticamente o tipo de retorno para a chamada indexada da função '"
                        + funDef.id + "' com múltiplos retornos e índice não literal.");
            }
        }

        exp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitExpField(ExpField exp) {
        Type targetType = exp.target.accept(this);

        if (!delta.containsKey(targetType.baseType)) {
            throw new RuntimeException("Linha " + exp.getLine() + ": " +
                    "Tentativa de acessar campo '" + exp.field + "' em um tipo não-data: " + targetType.baseType);
        }

        Data dataDef = delta.get(targetType.baseType);
        Type fieldType = null;

        if (dataDef instanceof DataRegular) {
            for (Decl decl : ((DataRegular) dataDef).declarations) {
                if (decl.id.equals(exp.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        } else if (dataDef instanceof DataAbstract) {
            for (Decl decl : ((DataAbstract) dataDef).declarations) {
                if (decl.id.equals(exp.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        }

        if (fieldType == null) {
            throw new RuntimeException("Linha " + exp.getLine() + ": O tipo '" + targetType.baseType
                    + "' não possui o campo '" + exp.field + "'.");
        }

        exp.expType = fieldType;
        return fieldType;
    }

    @Override
    public Type visitExpIndex(ExpIndex exp) {
        Type targetType = exp.target.accept(this);
        Type indexType = exp.index.accept(this);

        if (targetType.arrayDim == 0) {
            throw new RuntimeException(
                    "Linha " + exp.getLine() + ": Tentativa de indexar uma variável que não é um array.");
        }

        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException("Linha " + exp.getLine() + ": O índice de um array deve ser do tipo Int.");
        }

        Type resultType = new Type(targetType.baseType, targetType.arrayDim - 1);
        exp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitExpNew(ExpNew exp) {
        Type resultType;

        if (exp.size != null) {
            Type sizeType = exp.size.accept(this);
            if (!sizeType.baseType.equals("Int") || sizeType.arrayDim > 0) {
                throw new RuntimeException(
                        "Linha " + exp.getLine() + ": O tamanho de um novo array deve ser um inteiro.");
            }
            resultType = new Type(exp.type.baseType, exp.type.arrayDim + 1);
        } else {
            if (!delta.containsKey(exp.type.baseType)) {
                throw new RuntimeException(
                        "Linha " + exp.getLine() + ": Tipo '" + exp.type.baseType + "' não definido.");
            }
            resultType = exp.type;
        }

        exp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitItCond(ItCond itCond) {
        return itCond.accept(this);
    }

    @Override
    public Type visitItCondExpr(ItCondExpr itCondExpr) {
        return itCondExpr.expression.accept(this);
    }

    @Override
    public Type visitItCondLabelled(ItCondLabelled itCondLabelled) {
        return itCondLabelled.expression.accept(this);
    }

    @Override
    public Type visitLValueField(LValueField lValueField) {
        Type targetType = lValueField.target.accept(this);

        if (!delta.containsKey(targetType.baseType)) {
            throw new RuntimeException("Linha " + lValueField.getLine() + ": Tentativa de acessar campo '"
                    + lValueField.field + "' em um tipo não-data: "
                    + targetType.baseType);
        }

        Type fieldType = null;

        Data dataDef = delta.get(targetType.baseType);
        if (dataDef instanceof DataRegular) {
            for (Decl decl : ((DataRegular) dataDef).declarations) {
                if (decl.id.equals(lValueField.field)) {
                    fieldType = decl.type;
                    break;
                }
            }
        }

        if (fieldType == null) {
            throw new RuntimeException("Linha " + lValueField.getLine() + ": " +
                    "O tipo '" + targetType.baseType + "' não possui o campo '" + lValueField.field + "'.");
        }

        lValueField.expType = fieldType;
        return fieldType;
    }

    @Override
    public Type visitLValueIndex(LValueIndex lValueIndex) {
        Type targetType = lValueIndex.target.accept(this);
        Type indexType = lValueIndex.index.accept(this);

        if (targetType.arrayDim == 0) {
            throw new RuntimeException(
                    "Linha " + lValueIndex.getLine() + ": Tentativa de indexar uma variável que não é um array.");
        }

        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException(
                    "Linha " + lValueIndex.getLine() + ": O índice de um array deve ser do tipo Int.");
        }

        Type resultType = new Type(targetType.baseType, targetType.arrayDim - 1);
        lValueIndex.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitParam(Param param) {
        return null;
    }

    @Override
    public Type visitType(Type type) {
        return type;
    }

    @Override
    public Type visitExp(Exp exp) {
        return exp.accept(this);
    }
}