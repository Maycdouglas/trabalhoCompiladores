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

/**
 * @brief Classe responsável por construir a Árvore Sintática Abstrata (AST)
 *        a partir da Árvore de Análise (Parse Tree) gerada pelo ANTLR.
 *        Ela percorre a Parse Tree e cria os nós da AST correspondentes.
 */
public class ASTBuilder extends langBaseVisitor<Object> {

    /**
     * @brief Visita o nó raiz do programa e constrói o nó Prog da AST.
     * @return O nó raiz da AST (Prog).
     */
    @Override
    public Prog visitProg(langParser.ProgContext ctx) {
        List<Def> defs = new ArrayList<>();
        for (var defCtx : ctx.def()) {
            defs.add((Def) visit(defCtx));
        }
        return new Prog(defs, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um nó de declaração de variável (utilizado em 'data') e
     *        constrói um nó Decl da AST.
     */
    @Override
    public Decl visitDecl(langParser.DeclContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Decl(id, type, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um nó de tipo e constrói um nó Type da AST, considerando a
     *        dimensão do array.
     */
    @Override
    public Type visitType(langParser.TypeContext ctx) {
        String baseType = ctx.btype().getText();
        int arrayDepth = ctx.LBRACK().size(); // cada par de colchetes conta como 1 dimensão
        return new Type(baseType, arrayDepth, ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma lista de tipos de retorno de uma função e a converte para
     *        uma lista de nós Type da AST.
     */
    @Override
    public List<Type> visitRetTypes(langParser.RetTypesContext ctx) {
        List<Type> types = new ArrayList<>();
        for (var t : ctx.type()) {
            types.add((Type) visit(t));
        }
        return types;
    }

    /**
     * @brief Visita um nó de definição, que pode ser uma função ou um tipo de dado,
     *        e delega para o visitor específico.
     */
    @Override
    public Def visitDef(langParser.DefContext ctx) {
        if (ctx.data() != null)
            return (Def) visit(ctx.data());
        else
            return (Def) visit(ctx.fun());
    }

    /**
     * @brief Visita um nó de definição de 'data' regular e constrói um nó
     *        DataRegular da AST.
     */
    @Override
    public DataRegular visitDataRegular(langParser.DataRegularContext ctx) {
        String name = ctx.TYID().getText();
        List<Decl> declarations = new ArrayList<>();
        for (var declCtx : ctx.decl()) {
            declarations.add((Decl) visit(declCtx));
        }
        return new DataRegular(name, declarations, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um nó de definição de 'abstract data' e constrói um nó
     *        DataAbstract da AST.
     */
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

        return new DataAbstract(name, declarations, functions, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um nó de definição de função e constrói um nó Fun da AST.
     */
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

        return new Fun(name, params, returnTypes, body, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um nó de parâmetro de função e constrói um nó Param da AST.
     */
    @Override
    public Param visitParam(langParser.ParamContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Param(id, type, ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma lista de parâmetros e a converte para uma lista de nós
     *        Param da AST.
     */
    @Override
    public List<Param> visitParams(langParser.ParamsContext ctx) {
        List<Param> list = new ArrayList<>();
        for (var p : ctx.param()) {
            list.add((Param) visit(p));
        }
        return list;
    }

    /**
     * @brief Visita um nó de comando genérico e delega para o visitor do comando
     *        específico (if, assign, etc.).
     */
    @Override
    public Cmd visitCmd(langParser.CmdContext ctx) {
        int line = ctx.getStart().getLine();

        if (ctx.block() != null)
            return (Cmd) visit(ctx.block());
        if (ctx.IF() != null && ctx.ELSE() != null)
            return new CmdIf((Exp) visit(ctx.exp(0)), (Cmd) visit(ctx.cmd(0)), (Cmd) visit(ctx.cmd(1)), line);
        if (ctx.IF() != null) {
            Exp cond = (Exp) visit(ctx.exp(0));
            Cmd thenBranch = (Cmd) visit(ctx.cmd(0));
            Cmd elseBranch = ctx.ELSE() != null ? (Cmd) visit(ctx.cmd(1)) : null;
            return new CmdIf(cond, thenBranch, elseBranch, line);
        }
        if (ctx.ITERATE() != null)
            return new CmdIterate((ItCond) visit(ctx.itcond()), (Cmd) visit(ctx.cmd(0)), line);
        if (ctx.READ() != null)
            return new CmdRead((LValue) visit(ctx.lvalue(0)), line);
        if (ctx.PRINT() != null)
            return new CmdPrint((Exp) visit(ctx.exp(0)), line);
        if (ctx.RETURN() != null) {
            List<Exp> exps = new ArrayList<>();
            for (var e : ctx.exp()) {
                exps.add((Exp) visit(e));
            }
            return new CmdReturn(exps, line);
        }
        if (ctx.ASSIGN() != null)
            return new CmdAssign((LValue) visit(ctx.lvalue(0)), (Exp) visit(ctx.exp(0)), line);
        if (ctx.ID() != null && ctx.LANGLE() == null) {
            String id = ctx.ID().getText();
            List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
            List<LValue> rets = new ArrayList<>(); // sem valores de retorno nesse caso
            return new CmdCall(id, args, rets, line);
        }
        if (ctx.ID() != null && ctx.LANGLE() != null) {
            String id = ctx.ID().getText();
            List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
            List<LValue> rets = new ArrayList<>();
            for (int i = 0; i < ctx.lvalue().size(); i++) {
                rets.add((LValue) visit(ctx.lvalue(i)));
            }
            return new CmdCall(id, args, rets, line);
        }

        return null;
    }

    /**
     * @brief Visita um nó de bloco de comandos e constrói um nó CmdBlock da AST.
     */
    @Override
    public Cmd visitBlock(langParser.BlockContext ctx) {
        List<Cmd> cmds = new ArrayList<>();
        for (var c : ctx.cmd()) {
            cmds.add((Cmd) visit(c));
        }
        return new CmdBlock(cmds, ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma condição de iteração rotulada (ex: i : v) e constrói um nó
     *        ItCondLabelled.
     */
    @Override
    public ItCond visitCondLabelled(langParser.CondLabelledContext ctx) {
        return new ItCondLabelled(ctx.ID().getText(), (Exp) visit(ctx.exp()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma condição de iteração baseada em expressão e constrói um nó
     *        ItCondExpr.
     */
    @Override
    public ItCond visitCondExpr(langParser.CondExprContext ctx) {
        return new ItCondExpr((Exp) visit(ctx.exp()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita um LValue de identificador (variável simples) e constrói um nó
     *        LValueId.
     */
    @Override
    public LValue visitIdLval(langParser.IdLvalContext ctx) {
        String id = ctx.ID().getText();
        int line = ctx.getStart().getLine();

        return new LValueId(id, line);
    }

    /**
     * @brief Visita um LValue de acesso a índice (ex: v[i]) e constrói um nó
     *        LValueIndex.
     */
    @Override
    public LValue visitIndexLval(langParser.IndexLvalContext ctx) {
        return new LValueIndex((LValue) visit(ctx.lvalue()), (Exp) visit(ctx.exp()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita um LValue de acesso a campo (ex: p.x) e constrói um nó
     *        LValueField.
     */
    @Override
    public LValue visitFieldLval(langParser.FieldLvalContext ctx) {
        return new LValueField((LValue) visit(ctx.lvalue()), ctx.ID().getText(), ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma lista de expressões (argumentos de função) e a converte
     *        para uma lista de nós Exp.
     */
    @Override
    public List<Exp> visitExpsList(langParser.ExpsListContext ctx) {
        List<Exp> exps = new ArrayList<>();
        for (var e : ctx.exp()) {
            exps.add((Exp) visit(e));
        }
        return exps;
    }

    /**
     * @brief Visita um literal inteiro e constrói um nó ExpInt.
     */
    @Override
    public Exp visitIntExpr(langParser.IntExprContext ctx) {
        int line = ctx.getStart().getLine();
        int value = Integer.parseInt(ctx.INT().getText());
        return new ExpInt(value, line);
    }

    /**
     * @brief Visita um literal de ponto flutuante e constrói um nó ExpFloat.
     */
    @Override
    public Exp visitFloatExpr(langParser.FloatExprContext ctx) {
        return new ExpFloat(Float.parseFloat(ctx.FLOAT().getText()), ctx.getStart().getLine());
    }

    /**
     * @brief Método auxiliar para converter um literal de caractere do ANTLR
     *        (incluindo escapes) para um char Java.
     * @return O caractere correspondente.
     */
    private char parseCharLiteral(String text) {
        if (text.length() < 3 || text.charAt(0) != '\'' || text.charAt(text.length() - 1) != '\'') {
            throw new RuntimeException("Literal de caractere inválido: " + text);
        }

        String inner = text.substring(1, text.length() - 1); // remove as aspas simples

        if (inner.startsWith("\\")) {
            if (inner.length() > 1) {
                if (inner.length() == 4 && Character.isDigit(inner.charAt(1)) && Character.isDigit(inner.charAt(2))
                        && Character.isDigit(inner.charAt(3))) {
                    String decimalString = inner.substring(1); // pega os 3 dígitos
                    int value = Integer.parseInt(decimalString, 10); // interpreta como DECIMAL
                    return (char) value;
                }
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
                        throw new RuntimeException("Caractere de escape desconhecido: " + inner);
                }
            }
        }

        if (inner.length() == 1) {
            return inner.charAt(0);
        }

        throw new RuntimeException("Literal de caractere inválido: " + text);
    }

    /**
     * @brief Visita um literal de caractere e constrói um nó ExpChar.
     */
    @Override
    public Exp visitCharExpr(langParser.CharExprContext ctx) {
        String text = ctx.CHAR().getText(); // exemplo: "'\\n'"
        char parsed = parseCharLiteral(text);
        return new ExpChar(parsed, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um literal booleano 'true' e constrói um nó ExpBool.
     */
    @Override
    public Exp visitTrueExpr(langParser.TrueExprContext ctx) {
        return new ExpBool(true, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um literal booleano 'false' e constrói um nó ExpBool.
     */
    @Override
    public Exp visitFalseExpr(langParser.FalseExprContext ctx) {
        return new ExpBool(false, ctx.getStart().getLine());
    }

    /**
     * @brief Visita um literal 'null' e constrói um nó ExpNull.
     */
    @Override
    public Exp visitNullExpr(langParser.NullExprContext ctx) {
        return new ExpNull(ctx.getStart().getLine());
    }

    /**
     * @brief Converte recursivamente um LValue (lado esquerdo de uma atribuição)
     *        para sua representação
     *        como uma expressão (Exp), permitindo seu uso em outros contextos.
     * @return A representação do LValue como um nó Exp.
     */
    private Exp visitLvalExprFromLValue(LValue lv) {
        int line = lv.getLine();
        if (lv instanceof LValueId) {
            return new ExpVar(((LValueId) lv).id, line);
        } else if (lv instanceof LValueIndex) {
            LValueIndex idx = (LValueIndex) lv;
            return new ExpIndex(visitLvalExprFromLValue(idx.target), idx.index, line);
        } else if (lv instanceof LValueField) {
            LValueField fld = (LValueField) lv;
            return new ExpField(visitLvalExprFromLValue(fld.target), fld.field, line);
        } else {
            throw new RuntimeException("Cannot convert LValue to Exp: " + lv.getClass().getSimpleName());
        }
    }

    /**
     * @brief Visita um LValue usado como expressão e o converte para o tipo Exp
     *        apropriado.
     */
    @Override
    public Exp visitLvalExpr(langParser.LvalExprContext ctx) {
        LValue lv = (LValue) visit(ctx.lvalue());
        int line = ctx.getStart().getLine();

        if (lv instanceof LValueId) {
            return new ExpVar(((LValueId) lv).id, line);
        } else if (lv instanceof LValueIndex) {
            LValueIndex idx = (LValueIndex) lv;
            return new ExpIndex((Exp) visitLvalExprFromLValue(idx.target), idx.index, line); // cria uma Exp para o
                                                                                             // objeto base
        } else if (lv instanceof LValueField) {
            LValueField fld = (LValueField) lv;
            return new ExpField((Exp) visitLvalExprFromLValue(fld.target), fld.field, line); // cria uma Exp para o
                                                                                             // objeto base
        } else {
            throw new RuntimeException("Unsupported LValue type: " + lv.getClass().getSimpleName());
        }
    }

    /**
     * @brief Visita uma expressão entre parênteses e constrói um nó ExpParen.
     */
    @Override
    public Exp visitParenExpr(langParser.ParenExprContext ctx) {
        return new ExpParen((Exp) visit(ctx.exp()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma expressão de alocação ('new') e constrói um nó ExpNew.
     */
    @Override
    public Exp visitNewExpr(langParser.NewExprContext ctx) {
        Type t = (Type) visit(ctx.type());
        Exp size = ctx.exp() != null ? (Exp) visit(ctx.exp()) : null;
        return new ExpNew(t, size, ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma chamada de função usada como expressão e constrói um nó
     *        ExpCall.
     */
    @Override
    public Exp visitCallExpr(langParser.CallExprContext ctx) {
        String id = ctx.ID().getText();
        List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
        return new ExpCall(id, args, ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma chamada de função com acesso a índice (múltiplos retornos)
     *        e constrói um nó ExpCallIndexed.
     */
    @Override
    public Exp visitCallIndexedExpr(langParser.CallIndexedExprContext ctx) {
        String id = ctx.ID().getText();
        int line = ctx.getStart().getLine();
        List<Exp> args = ctx.exps() != null ? (List<Exp>) visit(ctx.exps()) : new ArrayList<>();
        Exp index = (Exp) visit(ctx.exp());

        ExpCall call = new ExpCall(id, args, line);
        return new ExpCallIndexed(call, index, line);
    }

    /**
     * @brief Visita uma expressão de negação unária ('!') e constrói um nó
     *        ExpUnaryOp.
     */
    @Override
    public Exp visitNotExpr(langParser.NotExprContext ctx) {
        return new ExpUnaryOp("!", (Exp) visit(ctx.expMul()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma expressão de negação unária ('-') e constrói um nó
     *        ExpUnaryOp.
     */
    @Override
    public Exp visitNegExpr(langParser.NegExprContext ctx) {
        return new ExpUnaryOp("-", (Exp) visit(ctx.expMul()), ctx.getStart().getLine());
    }

    /**
     * @brief Visita uma regra que transita de uma expressão de multiplicação para
     *        uma primária.
     */
    @Override
    public Exp visitToPrimary(langParser.ToPrimaryContext ctx) {
        return (Exp) visit(ctx.expPrimary());
    }

    /**
     * @brief Visita uma expressão de adição/subtração, tratando a associatividade à
     *        esquerda.
     */
    @Override
    public Exp visitAddExpr(langParser.AddExprContext ctx) {
        Exp left = (Exp) visit(ctx.expMul(0));
        for (int i = 1; i < ctx.expMul().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expMul(i));
            left = new ExpBinOp(op, left, right, left.getLine());
        }
        return left;
    }

    /**
     * @brief Visita uma expressão de igualdade/desigualdade, tratando a
     *        associatividade.
     */
    @Override
    public Exp visitEqExpr(langParser.EqExprContext ctx) {
        Exp left = (Exp) visit(ctx.expAdd(0));

        for (int i = 1; i < ctx.expAdd().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expAdd(i));
            left = new ExpBinOp(op, left, right, left.getLine());
        }

        return left;
    }

    /**
     * @brief Visita uma expressão relacional (menor que), tratando a
     *        associatividade.
     */
    @Override
    public Exp visitRelExpr(langParser.RelExprContext ctx) {
        Exp left = (Exp) visit(ctx.expEq(0));

        for (int i = 1; i < ctx.expEq().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expEq(i));
            left = new ExpBinOp(op, left, right, left.getLine());
        }

        return left;
    }

    /**
     * @brief Visita uma expressão lógica 'E' (&&), tratando a associatividade.
     */
    @Override
    public Exp visitAndExpr(langParser.AndExprContext ctx) {
        Exp left = (Exp) visit(ctx.expRel(0));

        for (int i = 1; i < ctx.expRel().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expRel(i));
            left = new ExpBinOp(op, left, right, left.getLine());
        }

        return left;
    }

    /**
     * @brief Visita uma expressão lógica 'OU' (||), tratando a associatividade.
     */
    @Override
    public Exp visitOrExpr(langParser.OrExprContext ctx) {
        Exp left = (Exp) visit(ctx.expAnd(0));

        for (int i = 1; i < ctx.expAnd().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Exp right = (Exp) visit(ctx.expAnd(i));
            left = new ExpBinOp(op, left, right, left.getLine());
        }

        return left;
    }

    /**
     * @brief Visita o nó de topo de uma expressão, iniciando a cascata de visitas.
     */
    @Override
    public Exp visitExpTop(langParser.ExpTopContext ctx) {
        return (Exp) visit(ctx.expOr());
    }
}