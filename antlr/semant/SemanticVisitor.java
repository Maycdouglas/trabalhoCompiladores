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

    private Fun currentFunction = null;

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

    @Override
    public Type visitCmdAssign(CmdAssign cmdAssign) {
        Type rightType = cmdAssign.expression.accept(this);
        Type leftType = cmdAssign.target.accept(this);

        if (leftType == null) {
            if (cmdAssign.target instanceof LValueId) {
                LValueId lvalId = (LValueId) cmdAssign.target;
                currentScope().put(lvalId.id, rightType);
            } else {
                throw new RuntimeException("Atribuição inválida: o alvo da atribuição não foi declarado.");
            }
        } else {
            boolean compatible = false;

            if (leftType.baseType.equals(rightType.baseType) && leftType.arrayDim == rightType.arrayDim) {
                compatible = true;
            }

            if (rightType.baseType.equals("Null")) {
                if (delta.containsKey(leftType.baseType) || leftType.arrayDim > 0) {
                    compatible = true;
                }
            }

            if (!compatible) {
                String varName = (cmdAssign.target instanceof LValueId) ? ((LValueId) cmdAssign.target).id
                        : "expressão";
                throw new RuntimeException("Tipos incompatíveis na atribuição. A variável '" + varName +
                        "' é do tipo " + leftType.baseType + " mas recebeu " + rightType.baseType + ".");
            }
        }
        return null;
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
            throw new RuntimeException("Função '" + cmd.id + "' não definida.");
        }

        if (funDef.params.size() != cmd.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + cmd.id + "'. Esperava " +
                    funDef.params.size() + ", mas recebeu " + cmd.args.size() + ".");
        }

        for (int i = 0; i < funDef.params.size(); i++) {
            Type argType = cmd.args.get(i).accept(this);
            Type paramType = funDef.params.get(i).type;
            if (!argType.baseType.equals(paramType.baseType) || argType.arrayDim != paramType.arrayDim) {
                throw new RuntimeException(
                        "Tipo incorreto para o argumento " + (i + 1) + " na chamada da função '" + cmd.id + "'.");
            }
        }

        if (cmd.rets != null && !cmd.rets.isEmpty()) {
            if (cmd.rets.size() != funDef.retTypes.size()) {
                throw new RuntimeException("Número de variáveis de retorno (" + cmd.rets.size() +
                        ") é diferente do número de valores retornados (" + funDef.retTypes.size() +
                        ") pela função '" + cmd.id + "'.");
            }

            for (int i = 0; i < cmd.rets.size(); i++) {
                LValue targetLval = cmd.rets.get(i);
                Type returnType = funDef.retTypes.get(i);
                Type lvalType = targetLval.accept(this);

                if (targetLval instanceof LValueId) {
                    String varName = ((LValueId) targetLval).id;
                    if (lvalType == null) {
                        currentScope().put(varName, returnType);
                    } else {
                        if (!lvalType.baseType.equals(returnType.baseType)
                                || lvalType.arrayDim != returnType.arrayDim) {
                            throw new RuntimeException(
                                    "Incompatibilidade de tipo ao atribuir o retorno da função '" + cmd.id +
                                            "' à variável '" + varName + "'.");
                        }
                    }
                } else {
                    throw new UnsupportedOperationException(
                            "Atribuição de retorno múltiplo só suporta variáveis simples no momento.");
                }
            }
        }

        return null;
    }

    @Override
    public Type visitCmdIf(CmdIf cmd) {
        Type condType = cmd.condition.accept(this);
        if (!condType.baseType.equals("Bool")) {
            throw new RuntimeException("A condição de um 'if' deve ser do tipo Bool.");
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
                throw new RuntimeException(
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
                throw new RuntimeException("A condição de um laço 'iterate' sem rótulo deve ser do tipo Int ou Bool.");
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
    public Type visitCmdReturn(CmdReturn cmdReturn) {
        if (currentFunction == null) {
            throw new RuntimeException("Comando 'return' encontrado fora de uma função.");
        }

        if (cmdReturn.values.size() != currentFunction.retTypes.size()) {
            throw new RuntimeException("A função '" + currentFunction.id + "' esperava " +
                    currentFunction.retTypes.size() + " valores de retorno, mas recebeu " +
                    cmdReturn.values.size() + ".");
        }

        for (int i = 0; i < cmdReturn.values.size(); i++) {
            Type returnedType = cmdReturn.values.get(i).accept(this);
            Type expectedType = currentFunction.retTypes.get(i);

            boolean compatible = false;

            if (returnedType.baseType.equals(expectedType.baseType) && returnedType.arrayDim == expectedType.arrayDim) {
                compatible = true;
            }

            if (returnedType.baseType.equals("Null")) {
                if (delta.containsKey(expectedType.baseType) || expectedType.arrayDim > 0) {
                    compatible = true;
                }
            }

            if (!compatible) {
                throw new RuntimeException(
                        "Tipo de retorno inválido na posição " + i + " para a função '" + currentFunction.id +
                                "'. Esperava " + expectedType.baseType + " mas recebeu " + returnedType.baseType + ".");
            }
        }
        return null;
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
    public Type visitDataRegular(DataRegular data) {
        if (delta.containsKey(data.name)) {
            throw new RuntimeException("Tipo '" + data.name + "' já definido.");
        }

        Map<String, Type> fields = new HashMap<>();
        for (Decl decl : data.declarations) {
            if (fields.containsKey(decl.id)) {
                throw new RuntimeException("Campo '" + decl.id + "' já foi declarado no tipo '" + data.name + "'.");
            }
            fields.put(decl.id, decl.type);
        }

        delta.put(data.name, data);
        return null;
    }

    @Override
    public Type visitDataAbstract(DataAbstract data) {
        if (delta.containsKey(data.name)) {
            throw new RuntimeException("Tipo '" + data.name + "' já definido.");
        }

        Map<String, Type> fields = new HashMap<>();
        for (Decl decl : data.declarations) {
            if (fields.containsKey(decl.id)) {
                throw new RuntimeException("Campo '" + decl.id + "' já foi declarado no tipo '" + data.name + "'.");
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

        if (leftType.baseType.equals("Int") && rightType.baseType.equals("Int")) {
            switch (expBinOp.op) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                    resultType = new Type("Int", 0);
                    break;
                case "==":
                case "!=":
                case "<":
                    resultType = new Type("Bool", 0);
                    break;
            }
        }

        else if ((leftType.baseType.equals("Float") || leftType.baseType.equals("Int")) &&
                (rightType.baseType.equals("Float") || rightType.baseType.equals("Int"))) {
            switch (expBinOp.op) {
                case "+":
                case "-":
                case "*":
                case "/":
                    resultType = new Type("Float", 0);
                    break;
                case "==":
                case "!=":
                case "<":
                    resultType = new Type("Bool", 0);
                    break;
            }
        }

        else if (leftType.baseType.equals("Bool") && rightType.baseType.equals("Bool")) {
            switch (expBinOp.op) {
                case "&&":
                    resultType = new Type("Bool", 0);
                    break;
            }
        }

        else if (leftType.baseType.equals("Char") && rightType.baseType.equals("Char")) {
            switch (expBinOp.op) {
                case "==":
                case "!=":
                    resultType = new Type("Bool", 0);
                    break;
            }
        }

        else if (expBinOp.op.equals("==") || expBinOp.op.equals("!=")) {
            boolean leftIsNullable = delta.containsKey(leftType.baseType) || leftType.arrayDim > 0;
            boolean rightIsNullable = delta.containsKey(rightType.baseType) || rightType.arrayDim > 0;

            if ((leftIsNullable && rightType.baseType.equals("Null")) ||
                    (rightIsNullable && leftType.baseType.equals("Null"))) {
                resultType = new Type("Bool", 0);
            }
        }

        if (resultType == null) {
            throw new RuntimeException("Operação '" + expBinOp.op + "' inválida entre os tipos "
                    + leftType.baseType + " e " + rightType.baseType);
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
                    throw new RuntimeException("Operador '!' só pode ser aplicado a booleanos.");
                }
                resultType = new Type("Bool", 0);
                break;
            case "-":
                if (!expType.baseType.equals("Int") && !expType.baseType.equals("Float")) {
                    throw new RuntimeException("Operador '-' só pode ser aplicado a números.");
                }
                resultType = expType;
                break;
            default:
                throw new RuntimeException("Operador unário desconhecido: " + expUnaryOp.op);
        }

        expUnaryOp.expType = resultType;
        return resultType;
    }

    @Override
    public Type visitExpVar(ExpVar expVar) {
        expVar.expType = findVar(expVar.name);
        return expVar.expType;
    }

    @Override
    public Type visitLValueId(LValueId lValueId) {
        for (int i = gamma.size() - 1; i >= 0; i--) {
            Map<String, Type> scope = gamma.get(i);
            if (scope.containsKey(lValueId.id)) {
                return scope.get(lValueId.id);
            }
        }
        return null;
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
            throw new RuntimeException("Função '" + exp.id + "' não definida.");
        }

        if (funDef.params.size() != exp.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + exp.id + "'.");
        }

        if (funDef.retTypes.size() > 1) {
            throw new RuntimeException("Chamada de função com múltiplos retornos '" + exp.id + "' deve ser indexada.");
        }
        if (funDef.retTypes.isEmpty()) {
            throw new RuntimeException(
                    "Função '" + exp.id + "' não possui valor de retorno para ser usada em uma expressão.");
        }

        Type returnType = funDef.retTypes.get(0);
        exp.expType = returnType;
        return returnType;
    }

    @Override
    public Type visitExpCallIndexed(ExpCallIndexed exp) {
        Fun funDef = theta.get(exp.call.id);
        if (funDef == null) {
            throw new RuntimeException("Função '" + exp.call.id + "' não definida.");
        }

        if (funDef.params.size() != exp.call.args.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + exp.call.id + "'.");
        }
        for (int i = 0; i < funDef.params.size(); i++) {
            Type argType = exp.call.args.get(i).accept(this);
            Type paramType = funDef.params.get(i).type;
            if (!argType.baseType.equals(paramType.baseType) || argType.arrayDim != paramType.arrayDim) {
                throw new RuntimeException(
                        "Tipo incorreto para o argumento " + (i + 1) + " na chamada da função '" + exp.call.id + "'.");
            }
        }

        Type indexType = exp.index.accept(this);
        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException("O índice de um retorno de função deve ser do tipo Int.");
        }

        Type resultType = null;

        if (exp.index instanceof ExpInt) {
            int idx = ((ExpInt) exp.index).value;
            if (idx < 0 || idx >= funDef.retTypes.size()) {
                throw new RuntimeException(
                        "Índice de retorno " + idx + " fora dos limites para a função '" + funDef.id + "'.");
            }
            resultType = funDef.retTypes.get(idx);
        } else {
            if (funDef.retTypes.size() == 1) {
                resultType = funDef.retTypes.get(0);
            } else {
                throw new RuntimeException(
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
            throw new RuntimeException(
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
            throw new RuntimeException("O tipo '" + targetType.baseType + "' não possui o campo '" + exp.field + "'.");
        }

        exp.expType = fieldType;
        return fieldType;
    }

    @Override
    public Type visitExpIndex(ExpIndex exp) {
        Type targetType = exp.target.accept(this);
        Type indexType = exp.index.accept(this);

        if (targetType.arrayDim == 0) {
            throw new RuntimeException("Tentativa de indexar uma variável que não é um array.");
        }

        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException("O índice de um array deve ser do tipo Int.");
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
                throw new RuntimeException("O tamanho de um novo array deve ser um inteiro.");
            }
            resultType = new Type(exp.expType.baseType, exp.expType.arrayDim + 1);
        } else {
            if (!delta.containsKey(exp.expType.baseType)) {
                throw new RuntimeException("Tipo '" + exp.expType.baseType + "' não definido.");
            }
            resultType = exp.expType;
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
            throw new RuntimeException("Tentativa de acessar campo '" + lValueField.field + "' em um tipo não-data: "
                    + targetType.baseType);
        }

        Data dataDef = delta.get(targetType.baseType);

        if (dataDef instanceof DataRegular) {
            for (Decl decl : ((DataRegular) dataDef).declarations) {
                if (decl.id.equals(lValueField.field)) {
                    return decl.type;
                }
            }
        } else if (dataDef instanceof DataAbstract) {
            for (Decl decl : ((DataAbstract) dataDef).declarations) {
                if (decl.id.equals(lValueField.field)) {
                    return decl.type;
                }
            }
        }

        throw new RuntimeException(
                "O tipo '" + targetType.baseType + "' não possui o campo '" + lValueField.field + "'.");
    }

    @Override
    public Type visitLValueIndex(LValueIndex lValueIndex) {
        Type targetType = lValueIndex.target.accept(this);
        Type indexType = lValueIndex.index.accept(this);

        if (targetType.arrayDim == 0) {
            throw new RuntimeException("Tentativa de indexar uma variável que não é um array.");
        }

        if (!indexType.baseType.equals("Int") || indexType.arrayDim > 0) {
            throw new RuntimeException("O índice de um array deve ser do tipo Int.");
        }

        return new Type(targetType.baseType, targetType.arrayDim - 1);
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