// Generated from parser/lang.g4 by ANTLR 4.8

    package parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link langParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface langVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link langParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(langParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef(langParser.DefContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#data}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData(langParser.DataContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#dataAbstract}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataAbstract(langParser.DataAbstractContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#dataRegular}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRegular(langParser.DataRegularContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(langParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#fun}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFun(langParser.FunContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#retTypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetTypes(langParser.RetTypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(langParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(langParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(langParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#btype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBtype(langParser.BtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(langParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(langParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CondLabelled}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondLabelled(langParser.CondLabelledContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CondExpr}
	 * labeled alternative in {@link langParser#itcond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondExpr(langParser.CondExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpTop}
	 * labeled alternative in {@link langParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpTop(langParser.ExpTopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link langParser#expOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(langParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link langParser#expAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(langParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link langParser#expRel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(langParser.RelExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link langParser#expEq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(langParser.EqExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link langParser#expAdd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(langParser.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(langParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegExpr}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegExpr(langParser.NegExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToPrimary}
	 * labeled alternative in {@link langParser#expMul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToPrimary(langParser.ToPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LvalExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLvalExpr(langParser.LvalExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(langParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(langParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallIndexedExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallIndexedExpr(langParser.CallIndexedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueExpr(langParser.TrueExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseExpr(langParser.FalseExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpr(langParser.NullExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExpr(langParser.IntExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatExpr(langParser.FloatExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link langParser#expPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharExpr(langParser.CharExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdLval(langParser.IdLvalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldLval(langParser.FieldLvalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexLval}
	 * labeled alternative in {@link langParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexLval(langParser.IndexLvalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpsList}
	 * labeled alternative in {@link langParser#exps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpsList(langParser.ExpsListContext ctx);
}