// Generated from e:/Windows/Projetos/trabalhoCompiladores/antlr/v-ast/parser/lang.g4 by ANTLR 4.13.1

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
		DATA=10, RETURN=11, READ=12, PRINT=13, IF=14, ELSE=15, ITERATE=16, PLUS=17, 
		MINUS=18, MUL=19, DIV=20, MOD=21, EQ=22, NEQ=23, LT=24, GT=25, AND=26, 
		NOT=27, ASSIGN=28, DCOLON=29, COLON=30, SEMI=31, COMMA=32, DOT=33, LPAREN=34, 
		RPAREN=35, LBRACE=36, RBRACE=37, LBRACK=38, RBRACK=39, LINE_COMMENT=40, 
		BLOCK_COMMENT=41, WS=42;
	public static final int
		RULE_prog = 0, RULE_stmt = 1, RULE_expr = 2, RULE_term = 3, RULE_factor = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stmt", "expr", "term", "factor"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'true'", "'false'", "'null'", "'abstract'", 
			"'data'", "'return'", "'read'", "'print'", "'if'", "'else'", "'iterate'", 
			"'+'", "'-'", "'*'", "'/'", "'%'", "'=='", "'!='", "'<'", "'>'", "'&&'", 
			"'!'", "'='", "'::'", "':'", "';'", "','", "'.'", "'('", "')'", "'{'", 
			"'}'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ID", "TYID", "INT", "FLOAT", "CHAR", "TRUE", "FALSE", "NULL", 
			"ABSTRACT", "DATA", "RETURN", "READ", "PRINT", "IF", "ELSE", "ITERATE", 
			"PLUS", "MINUS", "MUL", "DIV", "MOD", "EQ", "NEQ", "LT", "GT", "AND", 
			"NOT", "ASSIGN", "DCOLON", "COLON", "SEMI", "COMMA", "DOT", "LPAREN", 
			"RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "LINE_COMMENT", "BLOCK_COMMENT", 
			"WS"
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
			setState(10);
			((ProgContext)_localctx).s1 = stmt();
			setState(11);
			match(SEMI);
			 ((ProgContext)_localctx).ast =  new StmtList(((ProgContext)_localctx).s1.ast.getLine(), ((ProgContext)_localctx).s1.ast.getCol(), ((ProgContext)_localctx).s1.ast); 
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID || _la==INT) {
				{
				{
				setState(13);
				((ProgContext)_localctx).s2 = stmt();
				setState(14);
				match(SEMI);
				 ((ProgContext)_localctx).ast =  new StmtList(((ProgContext)_localctx).s2.ast.getLine(), ((ProgContext)_localctx).s2.ast.getCol(), _localctx.ast, ((ProgContext)_localctx).s2.ast); 
				}
				}
				setState(21);
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
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(langParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			setState(30);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(22);
				((StmtContext)_localctx).ID = match(ID);
				setState(23);
				match(ASSIGN);
				setState(24);
				((StmtContext)_localctx).expr = expr();
				 ((StmtContext)_localctx).ast =  new Attr((((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getCharPositionInLine():0), new ID((((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getCharPositionInLine():0), (((StmtContext)_localctx).ID!=null?((StmtContext)_localctx).ID.getText():null)), ((StmtContext)_localctx).expr.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				((StmtContext)_localctx).expr = expr();
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
		public TermContext term;
		public Token op;
		public ExprContext expr;
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(langParser.PLUS, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expr);
		try {
			setState(40);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(32);
				((ExprContext)_localctx).term = term();
				setState(33);
				((ExprContext)_localctx).op = match(PLUS);
				setState(34);
				((ExprContext)_localctx).expr = expr();
				 ((ExprContext)_localctx).ast =  new Add((((ExprContext)_localctx).op!=null?((ExprContext)_localctx).op.getLine():0), (((ExprContext)_localctx).op!=null?((ExprContext)_localctx).op.getCharPositionInLine():0), ((ExprContext)_localctx).term.ast, ((ExprContext)_localctx).expr.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(37);
				((ExprContext)_localctx).term = term();
				 ((ExprContext)_localctx).ast =  ((ExprContext)_localctx).term.ast; 
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
	public static class TermContext extends ParserRuleContext {
		public Expr ast;
		public FactorContext factor;
		public Token op;
		public TermContext term;
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode MUL() { return getToken(langParser.MUL, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_term);
		try {
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(42);
				((TermContext)_localctx).factor = factor();
				setState(43);
				((TermContext)_localctx).op = match(MUL);
				setState(44);
				((TermContext)_localctx).term = term();
				 ((TermContext)_localctx).ast =  new Mul((((TermContext)_localctx).op!=null?((TermContext)_localctx).op.getLine():0), (((TermContext)_localctx).op!=null?((TermContext)_localctx).op.getCharPositionInLine():0), ((TermContext)_localctx).factor.ast, ((TermContext)_localctx).term.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				((TermContext)_localctx).factor = factor();
				 ((TermContext)_localctx).ast =  ((TermContext)_localctx).factor.ast; 
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
	public static class FactorContext extends ParserRuleContext {
		public Expr ast;
		public Token ID;
		public Token INT;
		public TerminalNode ID() { return getToken(langParser.ID, 0); }
		public TerminalNode INT() { return getToken(langParser.INT, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_factor);
		try {
			setState(56);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				((FactorContext)_localctx).ID = match(ID);
				 ((FactorContext)_localctx).ast =  new ID((((FactorContext)_localctx).ID!=null?((FactorContext)_localctx).ID.getLine():0), (((FactorContext)_localctx).ID!=null?((FactorContext)_localctx).ID.getCharPositionInLine():0), (((FactorContext)_localctx).ID!=null?((FactorContext)_localctx).ID.getText():null)); 
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(54);
				((FactorContext)_localctx).INT = match(INT);
				 ((FactorContext)_localctx).ast =  new Num((((FactorContext)_localctx).INT!=null?((FactorContext)_localctx).INT.getLine():0), (((FactorContext)_localctx).INT!=null?((FactorContext)_localctx).INT.getCharPositionInLine():0), Integer.parseInt((((FactorContext)_localctx).INT!=null?((FactorContext)_localctx).INT.getText():null))); 
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

	public static final String _serializedATN =
		"\u0004\u0001*;\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0005\u0000\u0012\b\u0000\n\u0000\f\u0000\u0015\t\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u001f\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002)\b"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u00033\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0003\u00049\b\u0004\u0001\u0004\u0000"+
		"\u0000\u0005\u0000\u0002\u0004\u0006\b\u0000\u0000:\u0000\n\u0001\u0000"+
		"\u0000\u0000\u0002\u001e\u0001\u0000\u0000\u0000\u0004(\u0001\u0000\u0000"+
		"\u0000\u00062\u0001\u0000\u0000\u0000\b8\u0001\u0000\u0000\u0000\n\u000b"+
		"\u0003\u0002\u0001\u0000\u000b\f\u0005\u001f\u0000\u0000\f\u0013\u0006"+
		"\u0000\uffff\uffff\u0000\r\u000e\u0003\u0002\u0001\u0000\u000e\u000f\u0005"+
		"\u001f\u0000\u0000\u000f\u0010\u0006\u0000\uffff\uffff\u0000\u0010\u0012"+
		"\u0001\u0000\u0000\u0000\u0011\r\u0001\u0000\u0000\u0000\u0012\u0015\u0001"+
		"\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0013\u0014\u0001"+
		"\u0000\u0000\u0000\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u0013\u0001"+
		"\u0000\u0000\u0000\u0016\u0017\u0005\u0001\u0000\u0000\u0017\u0018\u0005"+
		"\u001c\u0000\u0000\u0018\u0019\u0003\u0004\u0002\u0000\u0019\u001a\u0006"+
		"\u0001\uffff\uffff\u0000\u001a\u001f\u0001\u0000\u0000\u0000\u001b\u001c"+
		"\u0003\u0004\u0002\u0000\u001c\u001d\u0006\u0001\uffff\uffff\u0000\u001d"+
		"\u001f\u0001\u0000\u0000\u0000\u001e\u0016\u0001\u0000\u0000\u0000\u001e"+
		"\u001b\u0001\u0000\u0000\u0000\u001f\u0003\u0001\u0000\u0000\u0000 !\u0003"+
		"\u0006\u0003\u0000!\"\u0005\u0011\u0000\u0000\"#\u0003\u0004\u0002\u0000"+
		"#$\u0006\u0002\uffff\uffff\u0000$)\u0001\u0000\u0000\u0000%&\u0003\u0006"+
		"\u0003\u0000&\'\u0006\u0002\uffff\uffff\u0000\')\u0001\u0000\u0000\u0000"+
		"( \u0001\u0000\u0000\u0000(%\u0001\u0000\u0000\u0000)\u0005\u0001\u0000"+
		"\u0000\u0000*+\u0003\b\u0004\u0000+,\u0005\u0013\u0000\u0000,-\u0003\u0006"+
		"\u0003\u0000-.\u0006\u0003\uffff\uffff\u0000.3\u0001\u0000\u0000\u0000"+
		"/0\u0003\b\u0004\u000001\u0006\u0003\uffff\uffff\u000013\u0001\u0000\u0000"+
		"\u00002*\u0001\u0000\u0000\u00002/\u0001\u0000\u0000\u00003\u0007\u0001"+
		"\u0000\u0000\u000045\u0005\u0001\u0000\u000059\u0006\u0004\uffff\uffff"+
		"\u000067\u0005\u0003\u0000\u000079\u0006\u0004\uffff\uffff\u000084\u0001"+
		"\u0000\u0000\u000086\u0001\u0000\u0000\u00009\t\u0001\u0000\u0000\u0000"+
		"\u0005\u0013\u001e(28";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}