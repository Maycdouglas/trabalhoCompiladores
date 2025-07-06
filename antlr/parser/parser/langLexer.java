// Generated from parser/lang.g4 by ANTLR 4.8

    package parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class langLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ABSTRACT=1, DATA=2, TYID=3, ID=4, INT_TYPE=5, CHAR_TYPE=6, BOOL_TYPE=7, 
		FLOAT_TYPE=8, IF=9, ELSE=10, ITERATE=11, READ=12, PRINT=13, RETURN=14, 
		NEW=15, TRUE=16, FALSE=17, NULL=18, INT=19, FLOAT=20, CHAR=21, PLUS=22, 
		MINUS=23, MUL=24, DIV=25, MOD=26, EQ=27, NEQ=28, AND=29, NOT=30, ASSIGN=31, 
		DCOLON=32, COLON=33, SEMI=34, COMMA=35, DOT=36, LPAREN=37, RPAREN=38, 
		LBRACE=39, RBRACE=40, LBRACK=41, RBRACK=42, RANGLE=43, LANGLE=44, LINE_COMMENT=45, 
		BLOCK_COMMENT=46, WS=47;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ABSTRACT", "DATA", "TYID", "ID", "INT_TYPE", "CHAR_TYPE", "BOOL_TYPE", 
			"FLOAT_TYPE", "IF", "ELSE", "ITERATE", "READ", "PRINT", "RETURN", "NEW", 
			"TRUE", "FALSE", "NULL", "INT", "FLOAT", "CHAR", "PLUS", "MINUS", "MUL", 
			"DIV", "MOD", "EQ", "NEQ", "AND", "NOT", "ASSIGN", "DCOLON", "COLON", 
			"SEMI", "COMMA", "DOT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", 
			"RBRACK", "RANGLE", "LANGLE", "LINE_COMMENT", "BLOCK_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'abstract'", "'data'", null, null, "'Int'", "'Char'", "'Bool'", 
			"'Float'", "'if'", "'else'", "'iterate'", "'read'", "'print'", "'return'", 
			"'new'", "'true'", "'false'", "'null'", null, null, null, "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'=='", "'!='", "'&&'", "'!'", "'='", "'::'", "':'", 
			"';'", "','", "'.'", "'('", "')'", "'{'", "'}'", "'['", "']'", "'>'", 
			"'<'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ABSTRACT", "DATA", "TYID", "ID", "INT_TYPE", "CHAR_TYPE", "BOOL_TYPE", 
			"FLOAT_TYPE", "IF", "ELSE", "ITERATE", "READ", "PRINT", "RETURN", "NEW", 
			"TRUE", "FALSE", "NULL", "INT", "FLOAT", "CHAR", "PLUS", "MINUS", "MUL", 
			"DIV", "MOD", "EQ", "NEQ", "AND", "NOT", "ASSIGN", "DCOLON", "COLON", 
			"SEMI", "COMMA", "DOT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", 
			"RBRACK", "RANGLE", "LANGLE", "LINE_COMMENT", "BLOCK_COMMENT", "WS"
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


	public langLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "lang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\61\u013d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\7\4r\n\4\f\4\16\4u\13\4\3\5\3\5\7\5y\n"+
		"\5\f\5\16\5|\13\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\24\6\24\u00c9\n\24\r\24\16\24\u00ca\3\25\6\25\u00ce\n\25\r\25\16"+
		"\25\u00cf\3\25\3\25\6\25\u00d4\n\25\r\25\16\25\u00d5\3\25\3\25\6\25\u00da"+
		"\n\25\r\25\16\25\u00db\5\25\u00de\n\25\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\5\26\u00e8\n\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37"+
		"\3 \3 \3!\3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3"+
		"*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3.\3.\7.\u0122\n.\f.\16.\u0125\13.\3.\3."+
		"\3/\3/\3/\3/\7/\u012d\n/\f/\16/\u0130\13/\3/\3/\3/\3/\3/\3\60\6\60\u0138"+
		"\n\60\r\60\16\60\u0139\3\60\3\60\3\u012e\2\61\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S"+
		"+U,W-Y.[/]\60_\61\3\2\n\3\2C\\\6\2\62;C\\aac|\3\2c|\3\2\62;\6\2\f\f\17"+
		"\17))^^\b\2))^^ddppttvv\4\2\f\f\17\17\5\2\13\f\17\17\"\"\2\u0148\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\3a\3\2\2\2"+
		"\5j\3\2\2\2\7o\3\2\2\2\tv\3\2\2\2\13}\3\2\2\2\r\u0081\3\2\2\2\17\u0086"+
		"\3\2\2\2\21\u008b\3\2\2\2\23\u0091\3\2\2\2\25\u0094\3\2\2\2\27\u0099\3"+
		"\2\2\2\31\u00a1\3\2\2\2\33\u00a6\3\2\2\2\35\u00ac\3\2\2\2\37\u00b3\3\2"+
		"\2\2!\u00b7\3\2\2\2#\u00bc\3\2\2\2%\u00c2\3\2\2\2\'\u00c8\3\2\2\2)\u00dd"+
		"\3\2\2\2+\u00df\3\2\2\2-\u00eb\3\2\2\2/\u00ed\3\2\2\2\61\u00ef\3\2\2\2"+
		"\63\u00f1\3\2\2\2\65\u00f3\3\2\2\2\67\u00f5\3\2\2\29\u00f8\3\2\2\2;\u00fb"+
		"\3\2\2\2=\u00fe\3\2\2\2?\u0100\3\2\2\2A\u0102\3\2\2\2C\u0105\3\2\2\2E"+
		"\u0107\3\2\2\2G\u0109\3\2\2\2I\u010b\3\2\2\2K\u010d\3\2\2\2M\u010f\3\2"+
		"\2\2O\u0111\3\2\2\2Q\u0113\3\2\2\2S\u0115\3\2\2\2U\u0117\3\2\2\2W\u0119"+
		"\3\2\2\2Y\u011b\3\2\2\2[\u011d\3\2\2\2]\u0128\3\2\2\2_\u0137\3\2\2\2a"+
		"b\7c\2\2bc\7d\2\2cd\7u\2\2de\7v\2\2ef\7t\2\2fg\7c\2\2gh\7e\2\2hi\7v\2"+
		"\2i\4\3\2\2\2jk\7f\2\2kl\7c\2\2lm\7v\2\2mn\7c\2\2n\6\3\2\2\2os\t\2\2\2"+
		"pr\t\3\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2t\b\3\2\2\2us\3\2\2"+
		"\2vz\t\4\2\2wy\t\3\2\2xw\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\n\3\2"+
		"\2\2|z\3\2\2\2}~\7K\2\2~\177\7p\2\2\177\u0080\7v\2\2\u0080\f\3\2\2\2\u0081"+
		"\u0082\7E\2\2\u0082\u0083\7j\2\2\u0083\u0084\7c\2\2\u0084\u0085\7t\2\2"+
		"\u0085\16\3\2\2\2\u0086\u0087\7D\2\2\u0087\u0088\7q\2\2\u0088\u0089\7"+
		"q\2\2\u0089\u008a\7n\2\2\u008a\20\3\2\2\2\u008b\u008c\7H\2\2\u008c\u008d"+
		"\7n\2\2\u008d\u008e\7q\2\2\u008e\u008f\7c\2\2\u008f\u0090\7v\2\2\u0090"+
		"\22\3\2\2\2\u0091\u0092\7k\2\2\u0092\u0093\7h\2\2\u0093\24\3\2\2\2\u0094"+
		"\u0095\7g\2\2\u0095\u0096\7n\2\2\u0096\u0097\7u\2\2\u0097\u0098\7g\2\2"+
		"\u0098\26\3\2\2\2\u0099\u009a\7k\2\2\u009a\u009b\7v\2\2\u009b\u009c\7"+
		"g\2\2\u009c\u009d\7t\2\2\u009d\u009e\7c\2\2\u009e\u009f\7v\2\2\u009f\u00a0"+
		"\7g\2\2\u00a0\30\3\2\2\2\u00a1\u00a2\7t\2\2\u00a2\u00a3\7g\2\2\u00a3\u00a4"+
		"\7c\2\2\u00a4\u00a5\7f\2\2\u00a5\32\3\2\2\2\u00a6\u00a7\7r\2\2\u00a7\u00a8"+
		"\7t\2\2\u00a8\u00a9\7k\2\2\u00a9\u00aa\7p\2\2\u00aa\u00ab\7v\2\2\u00ab"+
		"\34\3\2\2\2\u00ac\u00ad\7t\2\2\u00ad\u00ae\7g\2\2\u00ae\u00af\7v\2\2\u00af"+
		"\u00b0\7w\2\2\u00b0\u00b1\7t\2\2\u00b1\u00b2\7p\2\2\u00b2\36\3\2\2\2\u00b3"+
		"\u00b4\7p\2\2\u00b4\u00b5\7g\2\2\u00b5\u00b6\7y\2\2\u00b6 \3\2\2\2\u00b7"+
		"\u00b8\7v\2\2\u00b8\u00b9\7t\2\2\u00b9\u00ba\7w\2\2\u00ba\u00bb\7g\2\2"+
		"\u00bb\"\3\2\2\2\u00bc\u00bd\7h\2\2\u00bd\u00be\7c\2\2\u00be\u00bf\7n"+
		"\2\2\u00bf\u00c0\7u\2\2\u00c0\u00c1\7g\2\2\u00c1$\3\2\2\2\u00c2\u00c3"+
		"\7p\2\2\u00c3\u00c4\7w\2\2\u00c4\u00c5\7n\2\2\u00c5\u00c6\7n\2\2\u00c6"+
		"&\3\2\2\2\u00c7\u00c9\t\5\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2"+
		"\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb(\3\2\2\2\u00cc\u00ce\t"+
		"\5\2\2\u00cd\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf"+
		"\u00d0\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d3\7\60\2\2\u00d2\u00d4\t"+
		"\5\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5"+
		"\u00d6\3\2\2\2\u00d6\u00de\3\2\2\2\u00d7\u00d9\7\60\2\2\u00d8\u00da\t"+
		"\5\2\2\u00d9\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00d9\3\2\2\2\u00db"+
		"\u00dc\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00cd\3\2\2\2\u00dd\u00d7\3\2"+
		"\2\2\u00de*\3\2\2\2\u00df\u00e7\7)\2\2\u00e0\u00e8\n\6\2\2\u00e1\u00e2"+
		"\7^\2\2\u00e2\u00e8\t\7\2\2\u00e3\u00e4\7^\2\2\u00e4\u00e5\t\5\2\2\u00e5"+
		"\u00e6\t\5\2\2\u00e6\u00e8\t\5\2\2\u00e7\u00e0\3\2\2\2\u00e7\u00e1\3\2"+
		"\2\2\u00e7\u00e3\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00ea\7)\2\2\u00ea"+
		",\3\2\2\2\u00eb\u00ec\7-\2\2\u00ec.\3\2\2\2\u00ed\u00ee\7/\2\2\u00ee\60"+
		"\3\2\2\2\u00ef\u00f0\7,\2\2\u00f0\62\3\2\2\2\u00f1\u00f2\7\61\2\2\u00f2"+
		"\64\3\2\2\2\u00f3\u00f4\7\'\2\2\u00f4\66\3\2\2\2\u00f5\u00f6\7?\2\2\u00f6"+
		"\u00f7\7?\2\2\u00f78\3\2\2\2\u00f8\u00f9\7#\2\2\u00f9\u00fa\7?\2\2\u00fa"+
		":\3\2\2\2\u00fb\u00fc\7(\2\2\u00fc\u00fd\7(\2\2\u00fd<\3\2\2\2\u00fe\u00ff"+
		"\7#\2\2\u00ff>\3\2\2\2\u0100\u0101\7?\2\2\u0101@\3\2\2\2\u0102\u0103\7"+
		"<\2\2\u0103\u0104\7<\2\2\u0104B\3\2\2\2\u0105\u0106\7<\2\2\u0106D\3\2"+
		"\2\2\u0107\u0108\7=\2\2\u0108F\3\2\2\2\u0109\u010a\7.\2\2\u010aH\3\2\2"+
		"\2\u010b\u010c\7\60\2\2\u010cJ\3\2\2\2\u010d\u010e\7*\2\2\u010eL\3\2\2"+
		"\2\u010f\u0110\7+\2\2\u0110N\3\2\2\2\u0111\u0112\7}\2\2\u0112P\3\2\2\2"+
		"\u0113\u0114\7\177\2\2\u0114R\3\2\2\2\u0115\u0116\7]\2\2\u0116T\3\2\2"+
		"\2\u0117\u0118\7_\2\2\u0118V\3\2\2\2\u0119\u011a\7@\2\2\u011aX\3\2\2\2"+
		"\u011b\u011c\7>\2\2\u011cZ\3\2\2\2\u011d\u011e\7/\2\2\u011e\u011f\7/\2"+
		"\2\u011f\u0123\3\2\2\2\u0120\u0122\n\b\2\2\u0121\u0120\3\2\2\2\u0122\u0125"+
		"\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0126\3\2\2\2\u0125"+
		"\u0123\3\2\2\2\u0126\u0127\b.\2\2\u0127\\\3\2\2\2\u0128\u0129\7}\2\2\u0129"+
		"\u012a\7/\2\2\u012a\u012e\3\2\2\2\u012b\u012d\13\2\2\2\u012c\u012b\3\2"+
		"\2\2\u012d\u0130\3\2\2\2\u012e\u012f\3\2\2\2\u012e\u012c\3\2\2\2\u012f"+
		"\u0131\3\2\2\2\u0130\u012e\3\2\2\2\u0131\u0132\7/\2\2\u0132\u0133\7\177"+
		"\2\2\u0133\u0134\3\2\2\2\u0134\u0135\b/\2\2\u0135^\3\2\2\2\u0136\u0138"+
		"\t\t\2\2\u0137\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u0137\3\2\2\2\u0139"+
		"\u013a\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013c\b\60\2\2\u013c`\3\2\2\2"+
		"\16\2sz\u00ca\u00cf\u00d5\u00db\u00dd\u00e7\u0123\u012e\u0139\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}