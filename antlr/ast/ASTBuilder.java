/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import parser.langBaseVisitor;
import parser.langParser;
import ast.*;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends langBaseVisitor<Object> {

    @Override
    public Prog visitProg(langParser.ProgContext ctx) {
        List<Def> defs = new ArrayList<>();
        for (var defCtx : ctx.def()) {
            defs.add((Def) visit(defCtx)); // cast explícito
        }
        return new Prog(defs);
    }

    @Override
    public Decl visitDecl(langParser.DeclContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Decl(id, type);
    }

    @Override
    public Type visitType(langParser.TypeContext ctx) {
        String baseType = ctx.btype().getText();
        int arrayDepth = ctx.LBRACK().size(); // cada par de colchetes conta como 1 dimensão
        return new Type(baseType, arrayDepth);
    }

    @Override
    public List<Type> visitRetTypes(langParser.RetTypesContext ctx) {
        List<Type> types = new ArrayList<>();
        for (var t : ctx.type()) {
            types.add((Type) visit(t));
        }
        return types;
    }

    @Override
    public Def visitDef(langParser.DefContext ctx) {
        if (ctx.data() != null)
            return (Def) visit(ctx.data());
        else
            return (Def) visit(ctx.fun());
    }

    @Override
    public DataRegular visitDataRegular(langParser.DataRegularContext ctx) {
        String name = ctx.TYID().getText();
        List<Decl> declarations = new ArrayList<>();
        for (var declCtx : ctx.decl()) {
            declarations.add((Decl) visit(declCtx));
        }
        return new DataRegular(name, declarations);
    }

    @Override
    public DataAbstract visitDataAbstract(langParser.DataAbstractContext ctx) {
        String name = ctx.TYID().getText();
        List<Decl> declarations = new ArrayList<>();
        List<Fun> functions = new ArrayList<>();

        for (var child : ctx.children) {
            if (child instanceof langParser.DeclContext)
                declarations.add((Decl) visit(child));
            else if (child instanceof langParser.FunContext)
                functions.add((Fun) visit(child));
        }

        return new DataAbstract(name, declarations, functions);
    }

    @Override
    public Fun visitFun(langParser.FunContext ctx) {
        String name = ctx.ID().getText();

        List<Param> params = new ArrayList<>();
        if (ctx.params() != null)
            params = (List<Param>) visit(ctx.params());

        List<Type> returnTypes = ctx.retTypes() != null ? visitRetTypes(ctx.retTypes()) : new ArrayList<>();
        if (ctx.retTypes() != null)
            returnTypes = (List<Type>) visit(ctx.retTypes());

        Cmd body = (Cmd) visit(ctx.cmd());

        return new Fun(name, params, returnTypes, body);
    }

    @Override
    public Param visitParam(langParser.ParamContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Param(id, type);
    }

    @Override
    public List<Param> visitParams(langParser.ParamsContext ctx) {
        List<Param> list = new ArrayList<>();
        for (var p : ctx.param()) {
            list.add((Param) visit(p));
        }
        return list;
    }

    @Override
    public Cmd visitCmd(langParser.CmdContext ctx) {
        if (ctx.block() != null)
            return (Cmd) visit(ctx.block());
        if (ctx.IF() != null && ctx.ELSE() != null)
            return new CmdIf((Exp) visit(ctx.exp(0)), (Cmd) visit(ctx.cmd(0)), (Cmd) visit(ctx.cmd(1)));
        if (ctx.IF() != null) {
            Exp cond = (Exp) visit(ctx.exp(0));
            Cmd thenBranch = (Cmd) visit(ctx.cmd(0));
            Cmd elseBranch = ctx.ELSE() != null ? (Cmd) visit(ctx.cmd(1)) : null;
            return new CmdIf(cond, thenBranch, elseBranch);
        }
        if (ctx.ITERATE() != null)
            return new CmdIterate((ItCond) visit(ctx.itcond()), (Cmd) visit(ctx.cmd(0)));
        if (ctx.READ() != null)
            return new CmdRead((LValue) visit(ctx.lvalue(0)));
        if (ctx.PRINT() != null)
            return new CmdPrint((Exp) visit(ctx.exp(0)));
        if (ctx.RETURN() != null) {
            List<Exp> exps = new ArrayList<>();
            for (var e : ctx.exp()) {
                exps.add((Exp) visit(e));
            }
            return new CmdReturn(exps);
        }
        if (ctx.ASSIGN() != null)
            return new CmdAssign((LValue) visit(ctx.lvalue(0)), (Exp) visit(ctx.exp(0)));
        if (ctx.ID() != null && ctx.LANGLE() == null) {
            String id = ctx.ID().getText();
            List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
            List<LValue> rets = new ArrayList<>(); // sem valores de retorno nesse caso
            return new CmdCall(id, args, rets);
        }
        if (ctx.ID() != null && ctx.LANGLE() != null) {
            String id = ctx.ID().getText();
            List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
            List<LValue> rets = new ArrayList<>();
            for (int i = 0; i < ctx.lvalue().size(); i++) {
                rets.add((LValue) visit(ctx.lvalue(i)));
            }
            return new CmdCall(id, args, rets);
        }

        return null;
    }

    @Override
    public Cmd visitBlock(langParser.BlockContext ctx) {
        List<Cmd> cmds = new ArrayList<>();
        for (var c : ctx.cmd()) {
            cmds.add((Cmd) visit(c));
        }
        return new CmdBlock(cmds);
    }

    @Override
    public ItCond visitCondLabelled(langParser.CondLabelledContext ctx) {
        return new ItCondLabelled(ctx.ID().getText(), (Exp) visit(ctx.exp()));
    }

    @Override
    public ItCond visitCondExpr(langParser.CondExprContext ctx) {
        return new ItCondExpr((Exp) visit(ctx.exp()));
    }

    @Override
    public LValue visitIdLval(langParser.IdLvalContext ctx) {
        return new LValueId(ctx.ID().getText());
    }

    @Override
    public LValue visitIndexLval(langParser.IndexLvalContext ctx) {
        return new LValueIndex((LValue) visit(ctx.lvalue()), (Exp) visit(ctx.exp()));
    }

    @Override
    public LValue visitFieldLval(langParser.FieldLvalContext ctx) {
        return new LValueField((LValue) visit(ctx.lvalue()), ctx.ID().getText());
    }

    @Override
    public List<Exp> visitExpsList(langParser.ExpsListContext ctx) {
        List<Exp> exps = new ArrayList<>();
        for (var e : ctx.exp()) {
            exps.add((Exp) visit(e));
        }
        return exps;
    }

    @Override
    public Exp visitIntExpr(langParser.IntExprContext ctx) {
        return new ExpInt(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public Exp visitFloatExpr(langParser.FloatExprContext ctx) {
        return new ExpFloat(Float.parseFloat(ctx.FLOAT().getText()));
    }

    private char parseCharLiteral(String text) {
        if (text.length() < 3 || text.charAt(0) != '\'' || text.charAt(text.length() - 1) != '\'') {
            throw new RuntimeException("Literal de caractere inválido: " + text);
        }

        String inner = text.substring(1, text.length() - 1); // remove as aspas simples

        switch (inner) {
            case "\\n":
                return '\n';
            case "\\t":
                return '\t';
            case "\\r":
                return '\r';
            case "\\'":
                return '\'';
            case "\\\\":
                return '\\';
            default:
                if (inner.length() == 1)
                    return inner.charAt(0);
                throw new RuntimeException("Caractere desconhecido: " + inner);
        }
    }

    @Override
    public Exp visitCharExpr(langParser.CharExprContext ctx) {
        String text = ctx.CHAR().getText(); // exemplo: "'\\n'"
        char parsed = parseCharLiteral(text);
        return new ExpChar(parsed);

    }

    @Override
    public Exp visitTrueExpr(langParser.TrueExprContext ctx) {
        return new ExpBool(true);
    }

    @Override
    public Exp visitFalseExpr(langParser.FalseExprContext ctx) {
        return new ExpBool(false);
    }

    @Override
    public Exp visitNullExpr(langParser.NullExprContext ctx) {
        return new ExpNull();
    }

    private Exp visitLvalExprFromLValue(LValue lv) {
        if (lv instanceof LValueId) {
            return new ExpVar(((LValueId) lv).id);
        } else if (lv instanceof LValueIndex) {
            LValueIndex idx = (LValueIndex) lv;
            return new ExpIndex(visitLvalExprFromLValue(idx.target), idx.index);
        } else if (lv instanceof LValueField) {
            LValueField fld = (LValueField) lv;
            return new ExpField(visitLvalExprFromLValue(fld.target), fld.field);
        } else {
            throw new RuntimeException("Cannot convert LValue to Exp: " + lv.getClass().getSimpleName());
        }
    }

    @Override
    public Exp visitLvalExpr(langParser.LvalExprContext ctx) {
        LValue lv = (LValue) visit(ctx.lvalue());

        if (lv instanceof LValueId) {
            return new ExpVar(((LValueId) lv).id);
        } else if (lv instanceof LValueIndex) {
            LValueIndex idx = (LValueIndex) lv;
            return new ExpIndex((Exp) visitLvalExprFromLValue(idx.target), idx.index); // cria uma Exp para o objeto
                                                                                       // base
        } else if (lv instanceof LValueField) {
            LValueField fld = (LValueField) lv;
            return new ExpField((Exp) visitLvalExprFromLValue(fld.target), fld.field); // cria uma Exp para o objeto
                                                                                       // base
        } else {
            throw new RuntimeException("Unsupported LValue type: " + lv.getClass().getSimpleName());
        }
    }

    @Override
    public Exp visitParenExpr(langParser.ParenExprContext ctx) {
        return new ExpParen((Exp) visit(ctx.exp()));
    }

    @Override
    public Exp visitNewExpr(langParser.NewExprContext ctx) {
        Type t = (Type) visit(ctx.type());
        Exp size = ctx.exp() != null ? (Exp) visit(ctx.exp()) : null;
        return new ExpNew(t, size);
    }

    @Override
    public Exp visitCallExpr(langParser.CallExprContext ctx) {
        String id = ctx.ID().getText();
        List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
        return new ExpCall(id, args);
    }

    @Override
    public Exp visitCallIndexedExpr(langParser.CallIndexedExprContext ctx) {
        String id = ctx.ID().getText();
        List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
        Exp index = (Exp) visit(ctx.exp());

        ExpCall call = new ExpCall(id, args);
        return new ExpCallIndexed(call, index);
    }

    @Override
    public Exp visitNotExpr(langParser.NotExprContext ctx) {
        return new ExpUnaryOp("!", (Exp) visit(ctx.expMul()));
    }

    @Override
    public Exp visitNegExpr(langParser.NegExprContext ctx) {
        return new ExpUnaryOp("-", (Exp) visit(ctx.expMul()));
    }

    @Override
    public Exp visitToPrimary(langParser.ToPrimaryContext ctx) {
        return (Exp) visit(ctx.expPrimary());
    }

    @Override
    public Exp visitAddExpr(langParser.AddExprContext ctx) {
        Exp left = (Exp) visit(ctx.expMul(0));
        for (int i = 1; i < ctx.expMul().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expMul(i));
            left = new ExpBinOp(op, left, right);
        }
        return left;
    }

    @Override
    public Exp visitEqExpr(langParser.EqExprContext ctx) {
        Exp left = (Exp) visit(ctx.expAdd(0));

        for (int i = 1; i < ctx.expAdd().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expAdd(i));
            left = new ExpBinOp(op, left, right);
        }

        return left;
    }

    @Override
    public Exp visitRelExpr(langParser.RelExprContext ctx) {
        Exp left = (Exp) visit(ctx.expEq(0));

        for (int i = 1; i < ctx.expEq().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expEq(i));
            left = new ExpBinOp(op, left, right);
        }

        return left;
    }

    @Override
    public Exp visitAndExpr(langParser.AndExprContext ctx) {
        Exp left = (Exp) visit(ctx.expRel(0));

        for (int i = 1; i < ctx.expRel().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expRel(i));
            left = new ExpBinOp(op, left, right);
        }

        return left;
    }

    @Override
    public Exp visitOrExpr(langParser.OrExprContext ctx) {
        Exp left = (Exp) visit(ctx.expAnd(0));

        for (int i = 1; i < ctx.expAnd().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expAnd(i));
            left = new ExpBinOp(op, left, right);
        }

        return left;
    }

    @Override
    public Exp visitExpTop(langParser.ExpTopContext ctx) {
        return (Exp) visit(ctx.expOr());
    }
}
