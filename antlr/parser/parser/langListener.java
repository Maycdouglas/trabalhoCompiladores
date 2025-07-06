// Generated from parser/lang.g4 by ANTLR 4.8

    package parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link langParser}.
 */
public interface langListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link langParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(langParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(langParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#def}.
	 * @param ctx the parse tree
	 */
	void enterDef(langParser.DefContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#def}.
	 * @param ctx the parse tree
	 */
	void exitDef(langParser.DefContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#data}.
	 * @param ctx the parse tree
	 */
	void enterData(langParser.DataContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#data}.
	 * @param ctx the parse tree
	 */
	void exitData(langParser.DataContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#dataAbstract}.
	 * @param ctx the parse tree
	 */
	void enterDataAbstract(langParser.DataAbstractContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#dataAbstract}.
	 * @param ctx the parse tree
	 */
	void exitDataAbstract(langParser.DataAbstractContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#dataRegular}.
	 * @param ctx the parse tree
	 */
	void enterDataRegular(langParser.DataRegularContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#dataRegular}.
	 * @param ctx the parse tree
	 */
	void exitDataRegular(langParser.DataRegularContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(langParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(langParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#fun}.
	 * @param ctx the parse tree
	 */
	void enterFun(langParser.FunContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#fun}.
	 * @param ctx the parse tree
	 */
	void exitFun(langParser.FunContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#retTypes}.
	 * @param ctx the parse tree
	 */
	void enterRetTypes(langParser.RetTypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#retTypes}.
	 * @param ctx the parse tree
	 */
	void exitRetTypes(langParser.RetTypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(langParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(langParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(langParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(langParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(langParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(langParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#btype}.
	 * @param ctx the parse tree
	 */
	void enterBtype(langParser.BtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#btype}.
	 * @param ctx the parse tree
	 */
	void exitBtype(langParser.BtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(langParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(langParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(langParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(langParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondLabelled}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 */
	void enterCondLabelled(langParser.CondLabelledContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondLabelled}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 */
	void exitCondLabelled(langParser.CondLabelledContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondExpr}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 */
	void enterCondExpr(langParser.CondExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondExpr}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 */
	void exitCondExpr(langParser.CondExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpTop}
	 * labeled alternative in {@link langParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExpTop(langParser.ExpTopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpTop}
	 * labeled alternative in {@link langParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExpTop(langParser.ExpTopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link langParser#expOr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(langParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link langParser#expOr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(langParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link langParser#expAnd}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(langParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link langParser#expAnd}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(langParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link langParser#expRel}.
	 * @param ctx the parse tree
	 */
	void enterRelExpr(langParser.RelExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link langParser#expRel}.
	 * @param ctx the parse tree
	 */
	void exitRelExpr(langParser.RelExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link langParser#expEq}.
	 * @param ctx the parse tree
	 */
	void enterEqExpr(langParser.EqExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link langParser#expEq}.
	 * @param ctx the parse tree
	 */
	void exitEqExpr(langParser.EqExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link langParser#expAdd}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(langParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link langParser#expAdd}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(langParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(langParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(langParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void enterNegExpr(langParser.NegExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void exitNegExpr(langParser.NegExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToPrimary}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void enterToPrimary(langParser.ToPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToPrimary}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 */
	void exitToPrimary(langParser.ToPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LvalExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterLvalExpr(langParser.LvalExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LvalExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitLvalExpr(langParser.LvalExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(langParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(langParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(langParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(langParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallIndexedExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterCallIndexedExpr(langParser.CallIndexedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallIndexedExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitCallIndexedExpr(langParser.CallIndexedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TrueExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTrueExpr(langParser.TrueExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TrueExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTrueExpr(langParser.TrueExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FalseExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterFalseExpr(langParser.FalseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FalseExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitFalseExpr(langParser.FalseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterNullExpr(langParser.NullExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitNullExpr(langParser.NullExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterIntExpr(langParser.IntExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitIntExpr(langParser.IntExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterFloatExpr(langParser.FloatExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitFloatExpr(langParser.FloatExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void enterCharExpr(langParser.CharExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 */
	void exitCharExpr(langParser.CharExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterIdLval(langParser.IdLvalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitIdLval(langParser.IdLvalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FieldLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterFieldLval(langParser.FieldLvalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FieldLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitFieldLval(langParser.FieldLvalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterIndexLval(langParser.IndexLvalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitIndexLval(langParser.IndexLvalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpsList}
	 * labeled alternative in {@link langParser#exps}.
	 * @param ctx the parse tree
	 */
	void enterExpsList(langParser.ExpsListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpsList}
	 * labeled alternative in {@link langParser#exps}.
	 * @param ctx the parse tree
	 */
	void exitExpsList(langParser.ExpsListContext ctx);
}