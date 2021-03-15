// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCLexer.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WACCLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, COMMENT=2, BEGIN=3, END=4, IS=5, SKIP_=6, READ=7, FREE=8, RETURN=9, 
		EXIT=10, PRINT=11, PRINTLN=12, IF=13, THEN=14, ELSE=15, FI=16, WHILE=17, 
		DO=18, DONE=19, CALL=20, NEWPAIR=21, FST=22, SND=23, PAIR=24, INT=25, 
		BOOL=26, CHAR=27, STRING=28, PLUS=29, MINUS=30, NOT=31, LEN=32, ORD=33, 
		CHR=34, MUL=35, DIV=36, MOD=37, GT=38, GE=39, LT=40, LE=41, EQ=42, NEQ=43, 
		AND=44, OR=45, INT_LITER=46, BOOL_LITER=47, CHAR_LITER=48, STR_LITER=49, 
		NULL=50, OPEN_PAREN=51, CLOSE_PAREN=52, OPEN_SQR_PAREN=53, CLOSE_SQR_PAREN=54, 
		COMMA=55, SEMICOLON=56, EQUAL=57, IDENT=58;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "COMMENT", "BEGIN", "END", "IS", "SKIP_", "READ", "FREE", "RETURN", 
			"EXIT", "PRINT", "PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", "DO", 
			"DONE", "CALL", "NEWPAIR", "FST", "SND", "PAIR", "INT", "BOOL", "CHAR", 
			"STRING", "PLUS", "MINUS", "NOT", "LEN", "ORD", "CHR", "MUL", "DIV", 
			"MOD", "GT", "GE", "LT", "LE", "EQ", "NEQ", "AND", "OR", "INT_LITER", 
			"BOOL_LITER", "CHAR_LITER", "STR_LITER", "DIGIT", "CHARACTER", "ESCAPED_CHAR", 
			"NULL", "OPEN_PAREN", "CLOSE_PAREN", "OPEN_SQR_PAREN", "CLOSE_SQR_PAREN", 
			"COMMA", "SEMICOLON", "EQUAL", "IDENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'begin'", "'end'", "'is'", "'skip'", "'read'", "'free'", 
			"'return'", "'exit'", "'print'", "'println'", "'if'", "'then'", "'else'", 
			"'fi'", "'while'", "'do'", "'done'", "'call'", "'newpair'", "'fst'", 
			"'snd'", "'pair'", "'int'", "'bool'", "'char'", "'string'", "'+'", "'-'", 
			"'!'", "'len'", "'ord'", "'chr'", "'*'", "'/'", "'%'", "'>'", "'>='", 
			"'<'", "'<='", "'=='", "'!='", "'&&'", "'||'", null, null, null, null, 
			"'null'", "'('", "')'", "'['", "']'", "','", "';'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "COMMENT", "BEGIN", "END", "IS", "SKIP_", "READ", "FREE", 
			"RETURN", "EXIT", "PRINT", "PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", 
			"DO", "DONE", "CALL", "NEWPAIR", "FST", "SND", "PAIR", "INT", "BOOL", 
			"CHAR", "STRING", "PLUS", "MINUS", "NOT", "LEN", "ORD", "CHR", "MUL", 
			"DIV", "MOD", "GT", "GE", "LT", "LE", "EQ", "NEQ", "AND", "OR", "INT_LITER", 
			"BOOL_LITER", "CHAR_LITER", "STR_LITER", "NULL", "OPEN_PAREN", "CLOSE_PAREN", 
			"OPEN_SQR_PAREN", "CLOSE_SQR_PAREN", "COMMA", "SEMICOLON", "EQUAL", "IDENT"
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


	public WACCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "WACCLexer.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2<\u0183\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\3\2\6\2\177\n\2\r\2\16\2\u0080\3\2\3\2\3\3\3\3\7\3\u0087\n\3\f"+
		"\3\16\3\u008a\13\3\3\3\5\3\u008d\n\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25"+
		"\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3!\3!\3\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3*\3*\3*\3+"+
		"\3+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\6/\u0143\n/\r/\16/\u0144\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u0150\n\60\3\61\3\61\3\61\3\61"+
		"\3\62\3\62\7\62\u0158\n\62\f\62\16\62\u015b\13\62\3\62\3\62\3\63\3\63"+
		"\3\64\3\64\3\64\5\64\u0164\n\64\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\67"+
		"\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\5>\u017c\n>\3>\7>\u017f\n"+
		">\f>\16>\u0182\13>\2\2?\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62"+
		"c\63e\2g\2i\2k\64m\65o\66q\67s8u9w:y;{<\3\2\t\5\2\13\f\17\17\"\"\4\2\f"+
		"\f\17\17\4\3\f\f\17\17\5\2$$))^^\13\2$$))\62\62^^ddhhppttvv\5\2C\\aac"+
		"|\6\2\62;C\\aac|\2\u0186\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3"+
		"\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2"+
		"\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\3"+
		"~\3\2\2\2\5\u0084\3\2\2\2\7\u0090\3\2\2\2\t\u0096\3\2\2\2\13\u009a\3\2"+
		"\2\2\r\u009d\3\2\2\2\17\u00a2\3\2\2\2\21\u00a7\3\2\2\2\23\u00ac\3\2\2"+
		"\2\25\u00b3\3\2\2\2\27\u00b8\3\2\2\2\31\u00be\3\2\2\2\33\u00c6\3\2\2\2"+
		"\35\u00c9\3\2\2\2\37\u00ce\3\2\2\2!\u00d3\3\2\2\2#\u00d6\3\2\2\2%\u00dc"+
		"\3\2\2\2\'\u00df\3\2\2\2)\u00e4\3\2\2\2+\u00e9\3\2\2\2-\u00f1\3\2\2\2"+
		"/\u00f5\3\2\2\2\61\u00f9\3\2\2\2\63\u00fe\3\2\2\2\65\u0102\3\2\2\2\67"+
		"\u0107\3\2\2\29\u010c\3\2\2\2;\u0113\3\2\2\2=\u0115\3\2\2\2?\u0117\3\2"+
		"\2\2A\u0119\3\2\2\2C\u011d\3\2\2\2E\u0121\3\2\2\2G\u0125\3\2\2\2I\u0127"+
		"\3\2\2\2K\u0129\3\2\2\2M\u012b\3\2\2\2O\u012d\3\2\2\2Q\u0130\3\2\2\2S"+
		"\u0132\3\2\2\2U\u0135\3\2\2\2W\u0138\3\2\2\2Y\u013b\3\2\2\2[\u013e\3\2"+
		"\2\2]\u0142\3\2\2\2_\u014f\3\2\2\2a\u0151\3\2\2\2c\u0155\3\2\2\2e\u015e"+
		"\3\2\2\2g\u0163\3\2\2\2i\u0165\3\2\2\2k\u0167\3\2\2\2m\u016c\3\2\2\2o"+
		"\u016e\3\2\2\2q\u0170\3\2\2\2s\u0172\3\2\2\2u\u0174\3\2\2\2w\u0176\3\2"+
		"\2\2y\u0178\3\2\2\2{\u017b\3\2\2\2}\177\t\2\2\2~}\3\2\2\2\177\u0080\3"+
		"\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0083"+
		"\b\2\2\2\u0083\4\3\2\2\2\u0084\u0088\7%\2\2\u0085\u0087\n\3\2\2\u0086"+
		"\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2"+
		"\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008d\t\4\2\2\u008c"+
		"\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\b\3\2\2\u008f\6\3\2\2\2"+
		"\u0090\u0091\7d\2\2\u0091\u0092\7g\2\2\u0092\u0093\7i\2\2\u0093\u0094"+
		"\7k\2\2\u0094\u0095\7p\2\2\u0095\b\3\2\2\2\u0096\u0097\7g\2\2\u0097\u0098"+
		"\7p\2\2\u0098\u0099\7f\2\2\u0099\n\3\2\2\2\u009a\u009b\7k\2\2\u009b\u009c"+
		"\7u\2\2\u009c\f\3\2\2\2\u009d\u009e\7u\2\2\u009e\u009f\7m\2\2\u009f\u00a0"+
		"\7k\2\2\u00a0\u00a1\7r\2\2\u00a1\16\3\2\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4"+
		"\7g\2\2\u00a4\u00a5\7c\2\2\u00a5\u00a6\7f\2\2\u00a6\20\3\2\2\2\u00a7\u00a8"+
		"\7h\2\2\u00a8\u00a9\7t\2\2\u00a9\u00aa\7g\2\2\u00aa\u00ab\7g\2\2\u00ab"+
		"\22\3\2\2\2\u00ac\u00ad\7t\2\2\u00ad\u00ae\7g\2\2\u00ae\u00af\7v\2\2\u00af"+
		"\u00b0\7w\2\2\u00b0\u00b1\7t\2\2\u00b1\u00b2\7p\2\2\u00b2\24\3\2\2\2\u00b3"+
		"\u00b4\7g\2\2\u00b4\u00b5\7z\2\2\u00b5\u00b6\7k\2\2\u00b6\u00b7\7v\2\2"+
		"\u00b7\26\3\2\2\2\u00b8\u00b9\7r\2\2\u00b9\u00ba\7t\2\2\u00ba\u00bb\7"+
		"k\2\2\u00bb\u00bc\7p\2\2\u00bc\u00bd\7v\2\2\u00bd\30\3\2\2\2\u00be\u00bf"+
		"\7r\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1\7k\2\2\u00c1\u00c2\7p\2\2\u00c2"+
		"\u00c3\7v\2\2\u00c3\u00c4\7n\2\2\u00c4\u00c5\7p\2\2\u00c5\32\3\2\2\2\u00c6"+
		"\u00c7\7k\2\2\u00c7\u00c8\7h\2\2\u00c8\34\3\2\2\2\u00c9\u00ca\7v\2\2\u00ca"+
		"\u00cb\7j\2\2\u00cb\u00cc\7g\2\2\u00cc\u00cd\7p\2\2\u00cd\36\3\2\2\2\u00ce"+
		"\u00cf\7g\2\2\u00cf\u00d0\7n\2\2\u00d0\u00d1\7u\2\2\u00d1\u00d2\7g\2\2"+
		"\u00d2 \3\2\2\2\u00d3\u00d4\7h\2\2\u00d4\u00d5\7k\2\2\u00d5\"\3\2\2\2"+
		"\u00d6\u00d7\7y\2\2\u00d7\u00d8\7j\2\2\u00d8\u00d9\7k\2\2\u00d9\u00da"+
		"\7n\2\2\u00da\u00db\7g\2\2\u00db$\3\2\2\2\u00dc\u00dd\7f\2\2\u00dd\u00de"+
		"\7q\2\2\u00de&\3\2\2\2\u00df\u00e0\7f\2\2\u00e0\u00e1\7q\2\2\u00e1\u00e2"+
		"\7p\2\2\u00e2\u00e3\7g\2\2\u00e3(\3\2\2\2\u00e4\u00e5\7e\2\2\u00e5\u00e6"+
		"\7c\2\2\u00e6\u00e7\7n\2\2\u00e7\u00e8\7n\2\2\u00e8*\3\2\2\2\u00e9\u00ea"+
		"\7p\2\2\u00ea\u00eb\7g\2\2\u00eb\u00ec\7y\2\2\u00ec\u00ed\7r\2\2\u00ed"+
		"\u00ee\7c\2\2\u00ee\u00ef\7k\2\2\u00ef\u00f0\7t\2\2\u00f0,\3\2\2\2\u00f1"+
		"\u00f2\7h\2\2\u00f2\u00f3\7u\2\2\u00f3\u00f4\7v\2\2\u00f4.\3\2\2\2\u00f5"+
		"\u00f6\7u\2\2\u00f6\u00f7\7p\2\2\u00f7\u00f8\7f\2\2\u00f8\60\3\2\2\2\u00f9"+
		"\u00fa\7r\2\2\u00fa\u00fb\7c\2\2\u00fb\u00fc\7k\2\2\u00fc\u00fd\7t\2\2"+
		"\u00fd\62\3\2\2\2\u00fe\u00ff\7k\2\2\u00ff\u0100\7p\2\2\u0100\u0101\7"+
		"v\2\2\u0101\64\3\2\2\2\u0102\u0103\7d\2\2\u0103\u0104\7q\2\2\u0104\u0105"+
		"\7q\2\2\u0105\u0106\7n\2\2\u0106\66\3\2\2\2\u0107\u0108\7e\2\2\u0108\u0109"+
		"\7j\2\2\u0109\u010a\7c\2\2\u010a\u010b\7t\2\2\u010b8\3\2\2\2\u010c\u010d"+
		"\7u\2\2\u010d\u010e\7v\2\2\u010e\u010f\7t\2\2\u010f\u0110\7k\2\2\u0110"+
		"\u0111\7p\2\2\u0111\u0112\7i\2\2\u0112:\3\2\2\2\u0113\u0114\7-\2\2\u0114"+
		"<\3\2\2\2\u0115\u0116\7/\2\2\u0116>\3\2\2\2\u0117\u0118\7#\2\2\u0118@"+
		"\3\2\2\2\u0119\u011a\7n\2\2\u011a\u011b\7g\2\2\u011b\u011c\7p\2\2\u011c"+
		"B\3\2\2\2\u011d\u011e\7q\2\2\u011e\u011f\7t\2\2\u011f\u0120\7f\2\2\u0120"+
		"D\3\2\2\2\u0121\u0122\7e\2\2\u0122\u0123\7j\2\2\u0123\u0124\7t\2\2\u0124"+
		"F\3\2\2\2\u0125\u0126\7,\2\2\u0126H\3\2\2\2\u0127\u0128\7\61\2\2\u0128"+
		"J\3\2\2\2\u0129\u012a\7\'\2\2\u012aL\3\2\2\2\u012b\u012c\7@\2\2\u012c"+
		"N\3\2\2\2\u012d\u012e\7@\2\2\u012e\u012f\7?\2\2\u012fP\3\2\2\2\u0130\u0131"+
		"\7>\2\2\u0131R\3\2\2\2\u0132\u0133\7>\2\2\u0133\u0134\7?\2\2\u0134T\3"+
		"\2\2\2\u0135\u0136\7?\2\2\u0136\u0137\7?\2\2\u0137V\3\2\2\2\u0138\u0139"+
		"\7#\2\2\u0139\u013a\7?\2\2\u013aX\3\2\2\2\u013b\u013c\7(\2\2\u013c\u013d"+
		"\7(\2\2\u013dZ\3\2\2\2\u013e\u013f\7~\2\2\u013f\u0140\7~\2\2\u0140\\\3"+
		"\2\2\2\u0141\u0143\5e\63\2\u0142\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u0144"+
		"\u0142\3\2\2\2\u0144\u0145\3\2\2\2\u0145^\3\2\2\2\u0146\u0147\7v\2\2\u0147"+
		"\u0148\7t\2\2\u0148\u0149\7w\2\2\u0149\u0150\7g\2\2\u014a\u014b\7h\2\2"+
		"\u014b\u014c\7c\2\2\u014c\u014d\7n\2\2\u014d\u014e\7u\2\2\u014e\u0150"+
		"\7g\2\2\u014f\u0146\3\2\2\2\u014f\u014a\3\2\2\2\u0150`\3\2\2\2\u0151\u0152"+
		"\7)\2\2\u0152\u0153\5g\64\2\u0153\u0154\7)\2\2\u0154b\3\2\2\2\u0155\u0159"+
		"\7$\2\2\u0156\u0158\5g\64\2\u0157\u0156\3\2\2\2\u0158\u015b\3\2\2\2\u0159"+
		"\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015c\3\2\2\2\u015b\u0159\3\2"+
		"\2\2\u015c\u015d\7$\2\2\u015dd\3\2\2\2\u015e\u015f\4\62;\2\u015ff\3\2"+
		"\2\2\u0160\u0164\n\5\2\2\u0161\u0162\7^\2\2\u0162\u0164\5i\65\2\u0163"+
		"\u0160\3\2\2\2\u0163\u0161\3\2\2\2\u0164h\3\2\2\2\u0165\u0166\t\6\2\2"+
		"\u0166j\3\2\2\2\u0167\u0168\7p\2\2\u0168\u0169\7w\2\2\u0169\u016a\7n\2"+
		"\2\u016a\u016b\7n\2\2\u016bl\3\2\2\2\u016c\u016d\7*\2\2\u016dn\3\2\2\2"+
		"\u016e\u016f\7+\2\2\u016fp\3\2\2\2\u0170\u0171\7]\2\2\u0171r\3\2\2\2\u0172"+
		"\u0173\7_\2\2\u0173t\3\2\2\2\u0174\u0175\7.\2\2\u0175v\3\2\2\2\u0176\u0177"+
		"\7=\2\2\u0177x\3\2\2\2\u0178\u0179\7?\2\2\u0179z\3\2\2\2\u017a\u017c\t"+
		"\7\2\2\u017b\u017a\3\2\2\2\u017c\u0180\3\2\2\2\u017d\u017f\t\b\2\2\u017e"+
		"\u017d\3\2\2\2\u017f\u0182\3\2\2\2\u0180\u017e\3\2\2\2\u0180\u0181\3\2"+
		"\2\2\u0181|\3\2\2\2\u0182\u0180\3\2\2\2\r\2\u0080\u0088\u008c\u0144\u014f"+
		"\u0159\u0163\u017b\u017e\u0180\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}