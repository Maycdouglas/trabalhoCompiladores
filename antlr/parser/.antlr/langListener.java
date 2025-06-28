// Generated from e:/Windows/Projetos/trabalhoCompiladores/antlr/parser/lang.g4 by ANTLR 4.13.1
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
	 * Enter a parse tree produced by {@link langParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(langParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(langParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(langParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(langParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#rel_expr}.
	 * @param ctx the parse tree
	 */
	void enterRel_expr(langParser.Rel_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#rel_expr}.
	 * @param ctx the parse tree
	 */
	void exitRel_expr(langParser.Rel_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd_expr(langParser.Add_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd_expr(langParser.Add_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#mul_expr}.
	 * @param ctx the parse tree
	 */
	void enterMul_expr(langParser.Mul_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#mul_expr}.
	 * @param ctx the parse tree
	 */
	void exitMul_expr(langParser.Mul_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expr(langParser.Unary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expr(langParser.Unary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(langParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(langParser.AtomContext ctx);
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
	 * Enter a parse tree produced by {@link langParser#consList}.
	 * @param ctx the parse tree
	 */
	void enterConsList(langParser.ConsListContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#consList}.
	 * @param ctx the parse tree
	 */
	void exitConsList(langParser.ConsListContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(langParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(langParser.ConstructorContext ctx);
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
	 * Enter a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(langParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(langParser.TypeContext ctx);
}