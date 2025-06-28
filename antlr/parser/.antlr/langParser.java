// Generated from e:/Windows/Projetos/trabalhoCompiladores/antlr/parser/lang.g4 by ANTLR 4.13.1

    package parser;
    import ast.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class langParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ID=1, TYID=2, INT=3, FLOAT=4, CHAR=5, TRUE=6, FALSE=7, NULL=8, ABSTRACT=9, 
		DATA=10, RETURN=11, READ=12, PRINT=13, IF=14, ELSE=15, ITERATE=16, FUN=17, 
		PLUS=18, MINUS=19, MUL=20, DIV=21, MOD=22, EQ=23, NEQ=24, LT=25, AND=26, 
		NOT=27, ASSIGN=28, DCOLON=29, COLON=30, SEMI=31, COMMA=32, DOT=33, PIPE=34, 
		ARROW=35, LPAREN=36, RPAREN=37, LBRACE=38, RBRACE=39, LBRACK=40, RBRACK=41, 
		LINE_COMMENT=42, BLOCK_COMMENT=43, WS=44;
	public static final int
		RULE_prog = 0, RULE_stmt = 1, RULE_expr = 2, RULE_rel_expr = 3, RULE_add_expr = 4, 
		RULE_mul_expr = 5, RULE_unary_expr = 6, RULE_atom = 7, RULE_constructor = 8, 
		RULE_fun = 9, RULE_params = 10, RULE_type = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stmt", "expr", "rel_expr", "add_expr", "mul_expr", "unary_expr", 
			"atom", "constructor", "fun", "params", "type"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'true'", "'false'", "'null'", "'abstract'", 
			"'data'", "'return'", "'read'", "'print'", "'if'", "'else'", "'iterate'", 
			"'fun'", "'+'", "'-'", "'*'", "'/'", "'%'", "'=='", "'!='", "'<'", "'&&'", 
			"'!'", "'='", "'::'", "':'", "';'", "','", "'.'", "'|'", "'->'", "'('", 
			"')'", "'{'", "'}'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ID", "TYID", "INT", "FLOAT", "CHAR", "TRUE", "FALSE", "NULL", 
			"ABSTRACT", "DATA", "RETURN", "READ", "PRINT", "IF", "ELSE", "ITERATE", 
			"FUN", "PLUS", "MINUS", "MUL", "DIV", "MOD", "EQ", "NEQ", "LT", "AND", 
			"NOT", "ASSIGN", "DCOLON", "COLON", "SEMI", "COMMA", "DOT", "PIPE", "ARROW", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "LINE_COMMENT", 
			"BLOCK_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "lang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public langParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public StmtList ast;
		public StmtContext s1;
		public StmtContext s2;
		public List<TerminalNode> SEMI() { return getTokens(langParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(langParser.SEMI, i);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			((ProgContext)_localctx).s1 = stmt();
			setState(25);
			match(SEMI);
			 ((ProgContext)_localctx).ast =  new StmtList(((ProgContext)_localctx).s1.ast.getLine(), ((ProgContext)_localctx).s1.ast.getCol(), ((ProgContext)_localctx).s1.ast); 
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 68853782778L) != 0)) {
				{
				{
				setState(27);
				((ProgContext)_localctx).s2 = stmt();
				setState(28);
				match(SEMI);
				 ((ProgContext)_localctx).ast =  new StmtList(((ProgContext)_localctx).s2.ast.getLine(), ((ProgContext)_localctx).s2.ast.getCol(), _localctx.ast, ((ProgContext)_localctx).s2.ast); 
				}
				}
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public Node ast;
		public Token ID;
		public ExprContext expr;
		public Token RETURN;
		public Token READ;
		public Token IF;
		public StmtContext s1;
		public StmtContext s2;
		public StmtContext stmt;
		public Token ITERATE;
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(langParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RETURN() { return getToken(langParser.RETURN, 0); }
		public TerminalNode READ() { return getToken(langParser.READ, 0); }
		public TerminalNode IF() { return getToken(langParser.IF, 0); }
		public TerminalNode ELSE() { return getToken(langParser.ELSE, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public TerminalNode ITERATE() { return getToken(langParser.ITERATE, 0); }
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(36);
				((StmtContext)_localctx).ID = match(ID);
				setState(37);
				match(ASSIGN);
				setState(38);
				((StmtContext)_localctx).expr = expr(0);
				 ((StmtContext)_localctx).ast =  new Attr((((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getCharPositionInLine():0), new ID((((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getCharPositionInLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getText():null)), ((StmtContext)_localctx).expr.ast); 
						
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(41);
				((StmtContext)_localctx).RETURN = match(RETURN);
				setState(42);
				((StmtContext)_localctx).expr = expr(0);
				 ((StmtContext)_localctx).ast =  new Return((((StmtContext)_localctx).RETURN!=null?((StmtContext)_localctx).RETURN.getLine():0), (((StmtContext)_localctx).RETURN!=null?((StmtContext)_localctx).RETURN.getCharPositionInLine():0), ((StmtContext)_localctx).expr.ast); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(45);
				((StmtContext)_localctx).READ = match(READ);
				 ((StmtContext)_localctx).ast =  new Read((((StmtContext)_localctx).READ!=null?((StmtContext)_localctx).READ.getLine():0), (((StmtContext)_localctx).READ!=null?((StmtContext)_localctx).READ.getCharPositionInLine():0)); 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(47);
				((StmtContext)_localctx).IF = match(IF);
				setState(48);
				((StmtContext)_localctx).expr = expr(0);
				setState(49);
				((StmtContext)_localctx).s1 = stmt();
				setState(50);
				match(ELSE);
				setState(51);
				((StmtContext)_localctx).s2 = stmt();
				 ((StmtContext)_localctx).ast =  new If((((StmtContext)_localctx).IF!=null?((StmtContext)_localctx).IF.getLine():0), (((StmtContext)_localctx).IF!=null?((StmtContext)_localctx).IF.getCharPositionInLine():0), ((StmtContext)_localctx).expr.ast, ((StmtContext)_localctx).s1.ast, ((StmtContext)_localctx).s2.ast); 
						
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(54);
				((StmtContext)_localctx).IF = match(IF);
				setState(55);
				((StmtContext)_localctx).expr = expr(0);
				setState(56);
				((StmtContext)_localctx).stmt = stmt();
				 ((StmtContext)_localctx).ast =  new If((((StmtContext)_localctx).IF!=null?((StmtContext)_localctx).IF.getLine():0), (((StmtContext)_localctx).IF!=null?((StmtContext)_localctx).IF.getCharPositionInLine():0), ((StmtContext)_localctx).expr.ast, ((StmtContext)_localctx).stmt.ast); 
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(59);
				((StmtContext)_localctx).ITERATE = match(ITERATE);
				setState(60);
				((StmtContext)_localctx).expr = expr(0);
				setState(61);
				((StmtContext)_localctx).stmt = stmt();
				 ((StmtContext)_localctx).ast =  new Loop((((StmtContext)_localctx).ITERATE!=null?((StmtContext)_localctx).ITERATE.getLine():0), (((StmtContext)_localctx).ITERATE!=null?((StmtContext)_localctx).ITERATE.getCharPositionInLine():0), ((StmtContext)_localctx).expr.ast, ((StmtContext)_localctx).stmt.ast); 
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(64);
				((StmtContext)_localctx).expr = expr(0);
				 ((StmtContext)_localctx).ast =  new Print(((StmtContext)_localctx).expr.ast.getLine(), ((StmtContext)_localctx).expr.ast.getCol(), ((StmtContext)_localctx).expr.ast); 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public Expr ast;
		public ExprContext e1;
		public Rel_exprContext rel_expr;
		public Token op;
		public Rel_exprContext e2;
		public Rel_exprContext rel_expr() {
			return getRuleContext(Rel_exprContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode AND() { return getToken(langParser.AND, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(70);
			((ExprContext)_localctx).rel_expr = rel_expr(0);
			 ((ExprContext)_localctx).ast =  ((ExprContext)_localctx).rel_expr.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(80);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					_localctx.e1 = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(73);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(74);
					((ExprContext)_localctx).op = match(AND);
					setState(75);
					((ExprContext)_localctx).e2 = ((ExprContext)_localctx).rel_expr = rel_expr(0);
					 ((ExprContext)_localctx).ast =  new And((((ExprContext)_localctx).op!=null?((ExprContext)_localctx).op.getLine():0), (((ExprContext)_localctx).op!=null?((ExprContext)_localctx).op.getCharPositionInLine():0), ((ExprContext)_localctx).e1.ast, ((ExprContext)_localctx).e2.ast); 
					}
					} 
				}
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Rel_exprContext extends ParserRuleContext {
		public Expr ast;
		public Rel_exprContext e1;
		public Add_exprContext add_expr;
		public Token op;
		public Add_exprContext e2;
		public Add_exprContext add_expr() {
			return getRuleContext(Add_exprContext.class,0);
		}
		public Rel_exprContext rel_expr() {
			return getRuleContext(Rel_exprContext.class,0);
		}
		public TerminalNode EQ() { return getToken(langParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(langParser.NEQ, 0); }
		public TerminalNode LT() { return getToken(langParser.LT, 0); }
		public Rel_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rel_expr; }
	}

	public final Rel_exprContext rel_expr() throws RecognitionException {
		return rel_expr(0);
	}

	private Rel_exprContext rel_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Rel_exprContext _localctx = new Rel_exprContext(_ctx, _parentState);
		Rel_exprContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_rel_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(84);
			((Rel_exprContext)_localctx).add_expr = add_expr(0);
			 ((Rel_exprContext)_localctx).ast =  ((Rel_exprContext)_localctx).add_expr.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(102);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new Rel_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_rel_expr);
						setState(87);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(88);
						((Rel_exprContext)_localctx).op = match(EQ);
						setState(89);
						((Rel_exprContext)_localctx).e2 = ((Rel_exprContext)_localctx).add_expr = add_expr(0);
						 ((Rel_exprContext)_localctx).ast =  new Eq((((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getLine():0), (((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getCharPositionInLine():0), ((Rel_exprContext)_localctx).e1.ast, ((Rel_exprContext)_localctx).e2.ast); 
						}
						break;
					case 2:
						{
						_localctx = new Rel_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_rel_expr);
						setState(92);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(93);
						((Rel_exprContext)_localctx).op = match(NEQ);
						setState(94);
						((Rel_exprContext)_localctx).e2 = ((Rel_exprContext)_localctx).add_expr = add_expr(0);
						 ((Rel_exprContext)_localctx).ast =  new Neq((((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getLine():0), (((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getCharPositionInLine():0), ((Rel_exprContext)_localctx).e1.ast, ((Rel_exprContext)_localctx).e2.ast); 
						}
						break;
					case 3:
						{
						_localctx = new Rel_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_rel_expr);
						setState(97);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(98);
						((Rel_exprContext)_localctx).op = match(LT);
						setState(99);
						((Rel_exprContext)_localctx).e2 = ((Rel_exprContext)_localctx).add_expr = add_expr(0);
						 ((Rel_exprContext)_localctx).ast =  new Lt((((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getLine():0), (((Rel_exprContext)_localctx).op!=null?((Rel_exprContext)_localctx).op.getCharPositionInLine():0), ((Rel_exprContext)_localctx).e1.ast, ((Rel_exprContext)_localctx).e2.ast); 
						}
						break;
					}
					} 
				}
				setState(106);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Add_exprContext extends ParserRuleContext {
		public Expr ast;
		public Add_exprContext e1;
		public Mul_exprContext mul_expr;
		public Token op;
		public Mul_exprContext e2;
		public Mul_exprContext mul_expr() {
			return getRuleContext(Mul_exprContext.class,0);
		}
		public Add_exprContext add_expr() {
			return getRuleContext(Add_exprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(langParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(langParser.MINUS, 0); }
		public Add_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_add_expr; }
	}

	public final Add_exprContext add_expr() throws RecognitionException {
		return add_expr(0);
	}

	private Add_exprContext add_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Add_exprContext _localctx = new Add_exprContext(_ctx, _parentState);
		Add_exprContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_add_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(108);
			((Add_exprContext)_localctx).mul_expr = mul_expr(0);
			 ((Add_exprContext)_localctx).ast =  ((Add_exprContext)_localctx).mul_expr.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(123);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(121);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new Add_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_add_expr);
						setState(111);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(112);
						((Add_exprContext)_localctx).op = match(PLUS);
						setState(113);
						((Add_exprContext)_localctx).e2 = ((Add_exprContext)_localctx).mul_expr = mul_expr(0);
						 ((Add_exprContext)_localctx).ast =  new Add((((Add_exprContext)_localctx).op!=null?((Add_exprContext)_localctx).op.getLine():0), (((Add_exprContext)_localctx).op!=null?((Add_exprContext)_localctx).op.getCharPositionInLine():0), ((Add_exprContext)_localctx).e1.ast, ((Add_exprContext)_localctx).e2.ast); 
						}
						break;
					case 2:
						{
						_localctx = new Add_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_add_expr);
						setState(116);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(117);
						((Add_exprContext)_localctx).op = match(MINUS);
						setState(118);
						((Add_exprContext)_localctx).e2 = ((Add_exprContext)_localctx).mul_expr = mul_expr(0);
						 ((Add_exprContext)_localctx).ast =  new Sub((((Add_exprContext)_localctx).op!=null?((Add_exprContext)_localctx).op.getLine():0), (((Add_exprContext)_localctx).op!=null?((Add_exprContext)_localctx).op.getCharPositionInLine():0), ((Add_exprContext)_localctx).e1.ast, ((Add_exprContext)_localctx).e2.ast); 
						          		
						}
						break;
					}
					} 
				}
				setState(125);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Mul_exprContext extends ParserRuleContext {
		public Expr ast;
		public Mul_exprContext e1;
		public Unary_exprContext unary_expr;
		public Token op;
		public Unary_exprContext e2;
		public Unary_exprContext unary_expr() {
			return getRuleContext(Unary_exprContext.class,0);
		}
		public Mul_exprContext mul_expr() {
			return getRuleContext(Mul_exprContext.class,0);
		}
		public TerminalNode MUL() { return getToken(langParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(langParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(langParser.MOD, 0); }
		public Mul_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mul_expr; }
	}

	public final Mul_exprContext mul_expr() throws RecognitionException {
		return mul_expr(0);
	}

	private Mul_exprContext mul_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Mul_exprContext _localctx = new Mul_exprContext(_ctx, _parentState);
		Mul_exprContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_mul_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(127);
			((Mul_exprContext)_localctx).unary_expr = unary_expr();
			 ((Mul_exprContext)_localctx).ast =  ((Mul_exprContext)_localctx).unary_expr.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(147);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(145);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						_localctx = new Mul_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_mul_expr);
						setState(130);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(131);
						((Mul_exprContext)_localctx).op = match(MUL);
						setState(132);
						((Mul_exprContext)_localctx).e2 = ((Mul_exprContext)_localctx).unary_expr = unary_expr();
						 ((Mul_exprContext)_localctx).ast =  new Mul((((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getLine():0), (((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getCharPositionInLine():0), ((Mul_exprContext)_localctx).e1.ast, ((Mul_exprContext)_localctx).e2.ast); 
						}
						break;
					case 2:
						{
						_localctx = new Mul_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_mul_expr);
						setState(135);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(136);
						((Mul_exprContext)_localctx).op = match(DIV);
						setState(137);
						((Mul_exprContext)_localctx).e2 = ((Mul_exprContext)_localctx).unary_expr = unary_expr();
						 ((Mul_exprContext)_localctx).ast =  new Div((((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getLine():0), (((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getCharPositionInLine():0), ((Mul_exprContext)_localctx).e1.ast, ((Mul_exprContext)_localctx).e2.ast); 
						          		
						}
						break;
					case 3:
						{
						_localctx = new Mul_exprContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_mul_expr);
						setState(140);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(141);
						((Mul_exprContext)_localctx).op = match(MOD);
						setState(142);
						((Mul_exprContext)_localctx).e2 = ((Mul_exprContext)_localctx).unary_expr = unary_expr();
						 ((Mul_exprContext)_localctx).ast =  new Mod((((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getLine():0), (((Mul_exprContext)_localctx).op!=null?((Mul_exprContext)_localctx).op.getCharPositionInLine():0), ((Mul_exprContext)_localctx).e1.ast, ((Mul_exprContext)_localctx).e2.ast); 
						          		
						}
						break;
					}
					} 
				}
				setState(149);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Unary_exprContext extends ParserRuleContext {
		public Expr ast;
		public Token NOT;
		public Unary_exprContext e;
		public AtomContext atom;
		public TerminalNode NOT() { return getToken(langParser.NOT, 0); }
		public Unary_exprContext unary_expr() {
			return getRuleContext(Unary_exprContext.class,0);
		}
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public Unary_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expr; }
	}

	public final Unary_exprContext unary_expr() throws RecognitionException {
		Unary_exprContext _localctx = new Unary_exprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_unary_expr);
		try {
			setState(157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(150);
				((Unary_exprContext)_localctx).NOT = match(NOT);
				setState(151);
				((Unary_exprContext)_localctx).e = unary_expr();
				 ((Unary_exprContext)_localctx).ast =  new Not((((Unary_exprContext)_localctx).NOT!=null?((Unary_exprContext)_localctx).NOT.getLine():0), (((Unary_exprContext)_localctx).NOT!=null?((Unary_exprContext)_localctx).NOT.getCharPositionInLine():0), ((Unary_exprContext)_localctx).e.ast); 
				}
				break;
			case ID:
			case INT:
			case FLOAT:
			case CHAR:
			case TRUE:
			case FALSE:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(154);
				((Unary_exprContext)_localctx).atom = atom();
				 ((Unary_exprContext)_localctx).ast =  ((Unary_exprContext)_localctx).atom.ast; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtomContext extends ParserRuleContext {
		public Expr ast;
		public Token ID;
		public Token INT;
		public Token FLOAT;
		public Token CHAR;
		public Token TRUE;
		public Token FALSE;
		public ExprContext expr;
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode INT() { return getToken(langParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(langParser.FLOAT, 0); }
		public TerminalNode CHAR() { return getToken(langParser.CHAR, 0); }
		public TerminalNode TRUE() { return getToken(langParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(langParser.FALSE, 0); }
		public TerminalNode LPAREN() { return getToken(langParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(langParser.RPAREN, 0); }
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_atom);
		try {
			setState(176);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(159);
				((AtomContext)_localctx).ID = match(ID);
				 ((AtomContext)_localctx).ast =  new ID((((AtomContext)_localctx).ID!=null?((AtomContext)_localctx).ID.getLine():0), (((AtomContext)_localctx).ID!=null?((AtomContext)_localctx).ID.getCharPositionInLine():0), (((AtomContext)_localctx).ID!=null?((AtomContext)_localctx).ID.getText():null)); 
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				((AtomContext)_localctx).INT = match(INT);
				 ((AtomContext)_localctx).ast =  new Num((((AtomContext)_localctx).INT!=null?((AtomContext)_localctx).INT.getLine():0), (((AtomContext)_localctx).INT!=null?((AtomContext)_localctx).INT.getCharPositionInLine():0), Integer.parseInt((((AtomContext)_localctx).INT!=null?((AtomContext)_localctx).INT.getText():null))); 
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 3);
				{
				setState(163);
				((AtomContext)_localctx).FLOAT = match(FLOAT);
				 ((AtomContext)_localctx).ast =  new Real((((AtomContext)_localctx).FLOAT!=null?((AtomContext)_localctx).FLOAT.getLine():0), (((AtomContext)_localctx).FLOAT!=null?((AtomContext)_localctx).FLOAT.getCharPositionInLine():0), Double.parseDouble((((AtomContext)_localctx).FLOAT!=null?((AtomContext)_localctx).FLOAT.getText():null))); 
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(165);
				((AtomContext)_localctx).CHAR = match(CHAR);
				 ((AtomContext)_localctx).ast =  new CharExpr((((AtomContext)_localctx).CHAR!=null?((AtomContext)_localctx).CHAR.getLine():0), (((AtomContext)_localctx).CHAR!=null?((AtomContext)_localctx).CHAR.getCharPositionInLine():0), (((AtomContext)_localctx).CHAR!=null?((AtomContext)_localctx).CHAR.getText():null)); 
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 5);
				{
				setState(167);
				((AtomContext)_localctx).TRUE = match(TRUE);
				 ((AtomContext)_localctx).ast =  new BoolExpr((((AtomContext)_localctx).TRUE!=null?((AtomContext)_localctx).TRUE.getLine():0), (((AtomContext)_localctx).TRUE!=null?((AtomContext)_localctx).TRUE.getCharPositionInLine():0), true); 
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 6);
				{
				setState(169);
				((AtomContext)_localctx).FALSE = match(FALSE);
				 ((AtomContext)_localctx).ast =  new BoolExpr((((AtomContext)_localctx).FALSE!=null?((AtomContext)_localctx).FALSE.getLine():0), (((AtomContext)_localctx).FALSE!=null?((AtomContext)_localctx).FALSE.getCharPositionInLine():0), false); 
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 7);
				{
				setState(171);
				match(LPAREN);
				setState(172);
				((AtomContext)_localctx).expr = expr(0);
				setState(173);
				match(RPAREN);
				 ((AtomContext)_localctx).ast =  ((AtomContext)_localctx).expr.ast; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorContext extends ParserRuleContext {
		public Constructor ast;
		public Token TYID;
		public TerminalNode TYID() { return getToken(langParser.TYID, 0); }
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_constructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			((ConstructorContext)_localctx).TYID = match(TYID);
			 ((ConstructorContext)_localctx).ast =  new Constructor((((ConstructorContext)_localctx).TYID!=null?((ConstructorContext)_localctx).TYID.getLine():0), (((ConstructorContext)_localctx).TYID!=null?((ConstructorContext)_localctx).TYID.getCharPositionInLine():0), (((ConstructorContext)_localctx).TYID!=null?((ConstructorContext)_localctx).TYID.getText():null)); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunContext extends ParserRuleContext {
		public Node ast;
		public Token ABSTRACT;
		public Token ID;
		public TypeContext type;
		public Token FUN;
		public ParamsContext params;
		public ExprContext expr;
		public TerminalNode ABSTRACT() { return getToken(langParser.ABSTRACT, 0); }
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode COLON() { return getToken(langParser.COLON, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode FUN() { return getToken(langParser.FUN, 0); }
		public ParamsContext params() {
			return getRuleContext(ParamsContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(langParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FunContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fun; }
	}

	public final FunContext fun() throws RecognitionException {
		FunContext _localctx = new FunContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fun);
		try {
			setState(194);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
				enterOuterAlt(_localctx, 1);
				{
				setState(181);
				((FunContext)_localctx).ABSTRACT = match(ABSTRACT);
				setState(182);
				((FunContext)_localctx).ID = match(ID);
				setState(183);
				match(COLON);
				setState(184);
				((FunContext)_localctx).type = type(0);
				 ((FunContext)_localctx).ast =  new AbstractFun((((FunContext)_localctx).ABSTRACT!=null?((FunContext)_localctx).ABSTRACT.getLine():0), (((FunContext)_localctx).ABSTRACT!=null?((FunContext)_localctx).ABSTRACT.getCharPositionInLine():0), (((FunContext)_localctx).ID!=null?((FunContext)_localctx).ID.getText():null), ((FunContext)_localctx).type.ast); 
						
				}
				break;
			case FUN:
				enterOuterAlt(_localctx, 2);
				{
				setState(187);
				((FunContext)_localctx).FUN = match(FUN);
				setState(188);
				((FunContext)_localctx).ID = match(ID);
				setState(189);
				((FunContext)_localctx).params = params();
				setState(190);
				match(ASSIGN);
				setState(191);
				((FunContext)_localctx).expr = expr(0);
				 ((FunContext)_localctx).ast =  new Fun((((FunContext)_localctx).FUN!=null?((FunContext)_localctx).FUN.getLine():0), (((FunContext)_localctx).FUN!=null?((FunContext)_localctx).FUN.getCharPositionInLine():0), (((FunContext)_localctx).ID!=null?((FunContext)_localctx).ID.getText():null), ((FunContext)_localctx).params.ast, ((FunContext)_localctx).expr.ast); 
						
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamsContext extends ParserRuleContext {
		public ParamList ast;
		public Token ID;
		public ParamsContext rest;
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public ParamsContext params() {
			return getRuleContext(ParamsContext.class,0);
		}
		public ParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_params; }
	}

	public final ParamsContext params() throws RecognitionException {
		ParamsContext _localctx = new ParamsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_params);
		try {
			setState(202);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(196);
				((ParamsContext)_localctx).ID = match(ID);
				 ((ParamsContext)_localctx).ast =  new ParamList((((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getLine():0), (((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getCharPositionInLine():0), (((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getText():null)); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(198);
				((ParamsContext)_localctx).ID = match(ID);
				setState(199);
				((ParamsContext)_localctx).rest = params();
				 ((ParamsContext)_localctx).ast =  new ParamList((((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getLine():0), (((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getCharPositionInLine():0), (((ParamsContext)_localctx).ID!=null?((ParamsContext)_localctx).ID.getText():null), ((ParamsContext)_localctx).rest.ast); 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public Type ast;
		public TypeContext t1;
		public Token ID;
		public TypeContext type;
		public TypeContext t2;
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(langParser.LPAREN, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(langParser.RPAREN, 0); }
		public TerminalNode ARROW() { return getToken(langParser.ARROW, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(205);
				((TypeContext)_localctx).ID = match(ID);
				 ((TypeContext)_localctx).ast =  new TypeName((((TypeContext)_localctx).ID!=null?((TypeContext)_localctx).ID.getLine():0), (((TypeContext)_localctx).ID!=null?((TypeContext)_localctx).ID.getCharPositionInLine():0), (((TypeContext)_localctx).ID!=null?((TypeContext)_localctx).ID.getText():null)); 
				}
				break;
			case LPAREN:
				{
				setState(207);
				match(LPAREN);
				setState(208);
				((TypeContext)_localctx).type = type(0);
				setState(209);
				match(RPAREN);
				 ((TypeContext)_localctx).ast =  ((TypeContext)_localctx).type.ast; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(221);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TypeContext(_parentctx, _parentState);
					_localctx.t1 = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(214);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(215);
					match(ARROW);
					setState(216);
					((TypeContext)_localctx).t2 = ((TypeContext)_localctx).type = type(2);
					 ((TypeContext)_localctx).ast =  new TypeArrow(((TypeContext)_localctx).t1.ast.getLine(), ((TypeContext)_localctx).t1.ast.getCol(), ((TypeContext)_localctx).t1.ast, ((TypeContext)_localctx).t2.ast); 
					          		
					}
					} 
				}
				setState(223);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 3:
			return rel_expr_sempred((Rel_exprContext)_localctx, predIndex);
		case 4:
			return add_expr_sempred((Add_exprContext)_localctx, predIndex);
		case 5:
			return mul_expr_sempred((Mul_exprContext)_localctx, predIndex);
		case 11:
			return type_sempred((TypeContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean rel_expr_sempred(Rel_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean add_expr_sempred(Add_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean mul_expr_sempred(Mul_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		case 8:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001,\u00e1\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0005\u0000 \b\u0000\n\u0000\f\u0000#\t\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001D\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002O\b"+
		"\u0002\n\u0002\f\u0002R\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003g\b\u0003\n\u0003"+
		"\f\u0003j\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004z\b\u0004\n\u0004"+
		"\f\u0004}\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u0092\b\u0005\n\u0005\f\u0005"+
		"\u0095\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0003\u0006\u009e\b\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00b1\b\u0007\u0001\b"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00c3\b\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00cb\b\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u00d5\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0005\u000b\u00dc\b\u000b\n\u000b\f\u000b\u00df"+
		"\t\u000b\u0001\u000b\u0000\u0005\u0004\u0006\b\n\u0016\f\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0000\u00ef\u0000\u0018"+
		"\u0001\u0000\u0000\u0000\u0002C\u0001\u0000\u0000\u0000\u0004E\u0001\u0000"+
		"\u0000\u0000\u0006S\u0001\u0000\u0000\u0000\bk\u0001\u0000\u0000\u0000"+
		"\n~\u0001\u0000\u0000\u0000\f\u009d\u0001\u0000\u0000\u0000\u000e\u00b0"+
		"\u0001\u0000\u0000\u0000\u0010\u00b2\u0001\u0000\u0000\u0000\u0012\u00c2"+
		"\u0001\u0000\u0000\u0000\u0014\u00ca\u0001\u0000\u0000\u0000\u0016\u00d4"+
		"\u0001\u0000\u0000\u0000\u0018\u0019\u0003\u0002\u0001\u0000\u0019\u001a"+
		"\u0005\u001f\u0000\u0000\u001a!\u0006\u0000\uffff\uffff\u0000\u001b\u001c"+
		"\u0003\u0002\u0001\u0000\u001c\u001d\u0005\u001f\u0000\u0000\u001d\u001e"+
		"\u0006\u0000\uffff\uffff\u0000\u001e \u0001\u0000\u0000\u0000\u001f\u001b"+
		"\u0001\u0000\u0000\u0000 #\u0001\u0000\u0000\u0000!\u001f\u0001\u0000"+
		"\u0000\u0000!\"\u0001\u0000\u0000\u0000\"\u0001\u0001\u0000\u0000\u0000"+
		"#!\u0001\u0000\u0000\u0000$%\u0005\u0001\u0000\u0000%&\u0005\u001c\u0000"+
		"\u0000&\'\u0003\u0004\u0002\u0000\'(\u0006\u0001\uffff\uffff\u0000(D\u0001"+
		"\u0000\u0000\u0000)*\u0005\u000b\u0000\u0000*+\u0003\u0004\u0002\u0000"+
		"+,\u0006\u0001\uffff\uffff\u0000,D\u0001\u0000\u0000\u0000-.\u0005\f\u0000"+
		"\u0000.D\u0006\u0001\uffff\uffff\u0000/0\u0005\u000e\u0000\u000001\u0003"+
		"\u0004\u0002\u000012\u0003\u0002\u0001\u000023\u0005\u000f\u0000\u0000"+
		"34\u0003\u0002\u0001\u000045\u0006\u0001\uffff\uffff\u00005D\u0001\u0000"+
		"\u0000\u000067\u0005\u000e\u0000\u000078\u0003\u0004\u0002\u000089\u0003"+
		"\u0002\u0001\u00009:\u0006\u0001\uffff\uffff\u0000:D\u0001\u0000\u0000"+
		"\u0000;<\u0005\u0010\u0000\u0000<=\u0003\u0004\u0002\u0000=>\u0003\u0002"+
		"\u0001\u0000>?\u0006\u0001\uffff\uffff\u0000?D\u0001\u0000\u0000\u0000"+
		"@A\u0003\u0004\u0002\u0000AB\u0006\u0001\uffff\uffff\u0000BD\u0001\u0000"+
		"\u0000\u0000C$\u0001\u0000\u0000\u0000C)\u0001\u0000\u0000\u0000C-\u0001"+
		"\u0000\u0000\u0000C/\u0001\u0000\u0000\u0000C6\u0001\u0000\u0000\u0000"+
		"C;\u0001\u0000\u0000\u0000C@\u0001\u0000\u0000\u0000D\u0003\u0001\u0000"+
		"\u0000\u0000EF\u0006\u0002\uffff\uffff\u0000FG\u0003\u0006\u0003\u0000"+
		"GH\u0006\u0002\uffff\uffff\u0000HP\u0001\u0000\u0000\u0000IJ\n\u0002\u0000"+
		"\u0000JK\u0005\u001a\u0000\u0000KL\u0003\u0006\u0003\u0000LM\u0006\u0002"+
		"\uffff\uffff\u0000MO\u0001\u0000\u0000\u0000NI\u0001\u0000\u0000\u0000"+
		"OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000PQ\u0001\u0000\u0000"+
		"\u0000Q\u0005\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000ST\u0006"+
		"\u0003\uffff\uffff\u0000TU\u0003\b\u0004\u0000UV\u0006\u0003\uffff\uffff"+
		"\u0000Vh\u0001\u0000\u0000\u0000WX\n\u0004\u0000\u0000XY\u0005\u0017\u0000"+
		"\u0000YZ\u0003\b\u0004\u0000Z[\u0006\u0003\uffff\uffff\u0000[g\u0001\u0000"+
		"\u0000\u0000\\]\n\u0003\u0000\u0000]^\u0005\u0018\u0000\u0000^_\u0003"+
		"\b\u0004\u0000_`\u0006\u0003\uffff\uffff\u0000`g\u0001\u0000\u0000\u0000"+
		"ab\n\u0002\u0000\u0000bc\u0005\u0019\u0000\u0000cd\u0003\b\u0004\u0000"+
		"de\u0006\u0003\uffff\uffff\u0000eg\u0001\u0000\u0000\u0000fW\u0001\u0000"+
		"\u0000\u0000f\\\u0001\u0000\u0000\u0000fa\u0001\u0000\u0000\u0000gj\u0001"+
		"\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000"+
		"i\u0007\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000kl\u0006\u0004"+
		"\uffff\uffff\u0000lm\u0003\n\u0005\u0000mn\u0006\u0004\uffff\uffff\u0000"+
		"n{\u0001\u0000\u0000\u0000op\n\u0003\u0000\u0000pq\u0005\u0012\u0000\u0000"+
		"qr\u0003\n\u0005\u0000rs\u0006\u0004\uffff\uffff\u0000sz\u0001\u0000\u0000"+
		"\u0000tu\n\u0002\u0000\u0000uv\u0005\u0013\u0000\u0000vw\u0003\n\u0005"+
		"\u0000wx\u0006\u0004\uffff\uffff\u0000xz\u0001\u0000\u0000\u0000yo\u0001"+
		"\u0000\u0000\u0000yt\u0001\u0000\u0000\u0000z}\u0001\u0000\u0000\u0000"+
		"{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000|\t\u0001\u0000\u0000"+
		"\u0000}{\u0001\u0000\u0000\u0000~\u007f\u0006\u0005\uffff\uffff\u0000"+
		"\u007f\u0080\u0003\f\u0006\u0000\u0080\u0081\u0006\u0005\uffff\uffff\u0000"+
		"\u0081\u0093\u0001\u0000\u0000\u0000\u0082\u0083\n\u0004\u0000\u0000\u0083"+
		"\u0084\u0005\u0014\u0000\u0000\u0084\u0085\u0003\f\u0006\u0000\u0085\u0086"+
		"\u0006\u0005\uffff\uffff\u0000\u0086\u0092\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\n\u0003\u0000\u0000\u0088\u0089\u0005\u0015\u0000\u0000\u0089\u008a"+
		"\u0003\f\u0006\u0000\u008a\u008b\u0006\u0005\uffff\uffff\u0000\u008b\u0092"+
		"\u0001\u0000\u0000\u0000\u008c\u008d\n\u0002\u0000\u0000\u008d\u008e\u0005"+
		"\u0016\u0000\u0000\u008e\u008f\u0003\f\u0006\u0000\u008f\u0090\u0006\u0005"+
		"\uffff\uffff\u0000\u0090\u0092\u0001\u0000\u0000\u0000\u0091\u0082\u0001"+
		"\u0000\u0000\u0000\u0091\u0087\u0001\u0000\u0000\u0000\u0091\u008c\u0001"+
		"\u0000\u0000\u0000\u0092\u0095\u0001\u0000\u0000\u0000\u0093\u0091\u0001"+
		"\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094\u000b\u0001"+
		"\u0000\u0000\u0000\u0095\u0093\u0001\u0000\u0000\u0000\u0096\u0097\u0005"+
		"\u001b\u0000\u0000\u0097\u0098\u0003\f\u0006\u0000\u0098\u0099\u0006\u0006"+
		"\uffff\uffff\u0000\u0099\u009e\u0001\u0000\u0000\u0000\u009a\u009b\u0003"+
		"\u000e\u0007\u0000\u009b\u009c\u0006\u0006\uffff\uffff\u0000\u009c\u009e"+
		"\u0001\u0000\u0000\u0000\u009d\u0096\u0001\u0000\u0000\u0000\u009d\u009a"+
		"\u0001\u0000\u0000\u0000\u009e\r\u0001\u0000\u0000\u0000\u009f\u00a0\u0005"+
		"\u0001\u0000\u0000\u00a0\u00b1\u0006\u0007\uffff\uffff\u0000\u00a1\u00a2"+
		"\u0005\u0003\u0000\u0000\u00a2\u00b1\u0006\u0007\uffff\uffff\u0000\u00a3"+
		"\u00a4\u0005\u0004\u0000\u0000\u00a4\u00b1\u0006\u0007\uffff\uffff\u0000"+
		"\u00a5\u00a6\u0005\u0005\u0000\u0000\u00a6\u00b1\u0006\u0007\uffff\uffff"+
		"\u0000\u00a7\u00a8\u0005\u0006\u0000\u0000\u00a8\u00b1\u0006\u0007\uffff"+
		"\uffff\u0000\u00a9\u00aa\u0005\u0007\u0000\u0000\u00aa\u00b1\u0006\u0007"+
		"\uffff\uffff\u0000\u00ab\u00ac\u0005$\u0000\u0000\u00ac\u00ad\u0003\u0004"+
		"\u0002\u0000\u00ad\u00ae\u0005%\u0000\u0000\u00ae\u00af\u0006\u0007\uffff"+
		"\uffff\u0000\u00af\u00b1\u0001\u0000\u0000\u0000\u00b0\u009f\u0001\u0000"+
		"\u0000\u0000\u00b0\u00a1\u0001\u0000\u0000\u0000\u00b0\u00a3\u0001\u0000"+
		"\u0000\u0000\u00b0\u00a5\u0001\u0000\u0000\u0000\u00b0\u00a7\u0001\u0000"+
		"\u0000\u0000\u00b0\u00a9\u0001\u0000\u0000\u0000\u00b0\u00ab\u0001\u0000"+
		"\u0000\u0000\u00b1\u000f\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005\u0002"+
		"\u0000\u0000\u00b3\u00b4\u0006\b\uffff\uffff\u0000\u00b4\u0011\u0001\u0000"+
		"\u0000\u0000\u00b5\u00b6\u0005\t\u0000\u0000\u00b6\u00b7\u0005\u0001\u0000"+
		"\u0000\u00b7\u00b8\u0005\u001e\u0000\u0000\u00b8\u00b9\u0003\u0016\u000b"+
		"\u0000\u00b9\u00ba\u0006\t\uffff\uffff\u0000\u00ba\u00c3\u0001\u0000\u0000"+
		"\u0000\u00bb\u00bc\u0005\u0011\u0000\u0000\u00bc\u00bd\u0005\u0001\u0000"+
		"\u0000\u00bd\u00be\u0003\u0014\n\u0000\u00be\u00bf\u0005\u001c\u0000\u0000"+
		"\u00bf\u00c0\u0003\u0004\u0002\u0000\u00c0\u00c1\u0006\t\uffff\uffff\u0000"+
		"\u00c1\u00c3\u0001\u0000\u0000\u0000\u00c2\u00b5\u0001\u0000\u0000\u0000"+
		"\u00c2\u00bb\u0001\u0000\u0000\u0000\u00c3\u0013\u0001\u0000\u0000\u0000"+
		"\u00c4\u00c5\u0005\u0001\u0000\u0000\u00c5\u00cb\u0006\n\uffff\uffff\u0000"+
		"\u00c6\u00c7\u0005\u0001\u0000\u0000\u00c7\u00c8\u0003\u0014\n\u0000\u00c8"+
		"\u00c9\u0006\n\uffff\uffff\u0000\u00c9\u00cb\u0001\u0000\u0000\u0000\u00ca"+
		"\u00c4\u0001\u0000\u0000\u0000\u00ca\u00c6\u0001\u0000\u0000\u0000\u00cb"+
		"\u0015\u0001\u0000\u0000\u0000\u00cc\u00cd\u0006\u000b\uffff\uffff\u0000"+
		"\u00cd\u00ce\u0005\u0001\u0000\u0000\u00ce\u00d5\u0006\u000b\uffff\uffff"+
		"\u0000\u00cf\u00d0\u0005$\u0000\u0000\u00d0\u00d1\u0003\u0016\u000b\u0000"+
		"\u00d1\u00d2\u0005%\u0000\u0000\u00d2\u00d3\u0006\u000b\uffff\uffff\u0000"+
		"\u00d3\u00d5\u0001\u0000\u0000\u0000\u00d4\u00cc\u0001\u0000\u0000\u0000"+
		"\u00d4\u00cf\u0001\u0000\u0000\u0000\u00d5\u00dd\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d7\n\u0001\u0000\u0000\u00d7\u00d8\u0005#\u0000\u0000\u00d8"+
		"\u00d9\u0003\u0016\u000b\u0002\u00d9\u00da\u0006\u000b\uffff\uffff\u0000"+
		"\u00da\u00dc\u0001\u0000\u0000\u0000\u00db\u00d6\u0001\u0000\u0000\u0000"+
		"\u00dc\u00df\u0001\u0000\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000"+
		"\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u0017\u0001\u0000\u0000\u0000"+
		"\u00df\u00dd\u0001\u0000\u0000\u0000\u000f!CPfhy{\u0091\u0093\u009d\u00b0"+
		"\u00c2\u00ca\u00d4\u00dd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}