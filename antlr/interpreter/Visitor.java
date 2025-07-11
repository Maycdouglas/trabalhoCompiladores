/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

import ast.*;

public interface Visitor<T> {
    T visitCmd(Cmd exp);
    T visitCmdAssign(CmdAssign cmd);
    T visitCmdBlock(CmdBlock cmd);
    T visitCmdCall(CmdCall cmd);
    T visitCmdIf(CmdIf cmd);
    T visitCmdIterate(CmdIterate cmd);
    T visitCmdPrint(CmdPrint cmd);
    T visitCmdRead(CmdRead cmd);
    T visitCmdReturn(CmdReturn cmd);
    T visitData(Data data);
    T visitDataAbstract(DataAbstract data);
    T visitDataRegular(DataRegular data);
    T visitDecl(Decl decl);
    T visitDef(Def def);
    T visitExp(Exp exp);
    T visitExpBinOp(ExpBinOp exp);
    T visitExpBool(ExpBool exp);
    T visitExpCall(ExpCall exp);
    T visitExpCallIndexed(ExpCallIndexed exp);
    T visitExpChar(ExpChar exp);
    T visitExpField(ExpField exp);
    T visitExpFloat(ExpFloat exp);
    T visitExpIndex(ExpIndex exp);
    T visitExpInt(ExpInt exp);
    T visitExpNew(ExpNew exp);
    T visitExpNull(ExpNull exp);
    T visitExpParen(ExpParen exp);
    T visitExpUnaryOp(ExpUnaryOp exp);
    T visitExpVar(ExpVar exp);
    T visitFun(Fun fun);
    T visitItCond(ItCond itCond);
    T visitItCondExpr(ItCondExpr itCondExpr);
    T visitItCondLabelled(ItCondLabelled itCondLabelled);
    T visitLValue(LValue lValue);
    T visitLValueField(LValueField lValueField);
    T visitLValueId(LValueId lValueId);
    T visitLValueIndex(LValueIndex lValueIndex);
    T visitParam(Param param);
    T visitProg(Prog prog);
    T visitType(Type type);
}
