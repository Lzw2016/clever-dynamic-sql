// Generated from C:/Users/lizw/Desktop/SourceCode/github/clever-dynamic-sql/src/main/resources\SqlFuncParser.g4 by ANTLR 4.9.2
package org.clever.dynamic.sql.dialect.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SqlFuncParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAREN=1, RPAREN=2, COMMA=3, IDENTIFIER=4, STRING_LITERAL=5, PARAMETER=6;
	public static final int
		RULE_funcDeclaration = 0, RULE_parameterList = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"funcDeclaration", "parameterList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LPAREN", "RPAREN", "COMMA", "IDENTIFIER", "STRING_LITERAL", "PARAMETER"
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
	public String getGrammarFileName() { return "SqlFuncParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SqlFuncParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FuncDeclarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SqlFuncParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(SqlFuncParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(SqlFuncParser.RPAREN, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FuncDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).enterFuncDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).exitFuncDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlFuncParserVisitor ) return ((SqlFuncParserVisitor<? extends T>)visitor).visitFuncDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDeclarationContext funcDeclaration() throws RecognitionException {
		FuncDeclarationContext _localctx = new FuncDeclarationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_funcDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4);
			match(IDENTIFIER);
			setState(5);
			match(LPAREN);
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PARAMETER) {
				{
				setState(6);
				parameterList();
				}
			}

			setState(9);
			match(RPAREN);
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

	public static class ParameterListContext extends ParserRuleContext {
		public List<TerminalNode> PARAMETER() { return getTokens(SqlFuncParser.PARAMETER); }
		public TerminalNode PARAMETER(int i) {
			return getToken(SqlFuncParser.PARAMETER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlFuncParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlFuncParser.COMMA, i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).exitParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlFuncParserVisitor ) return ((SqlFuncParserVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(11);
			match(PARAMETER);
			setState(16);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(12);
				match(COMMA);
				setState(13);
				match(PARAMETER);
				}
				}
				setState(18);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b\26\4\2\t\2\4\3"+
		"\t\3\3\2\3\2\3\2\5\2\n\n\2\3\2\3\2\3\3\3\3\3\3\7\3\21\n\3\f\3\16\3\24"+
		"\13\3\3\3\2\2\4\2\4\2\2\2\25\2\6\3\2\2\2\4\r\3\2\2\2\6\7\7\6\2\2\7\t\7"+
		"\3\2\2\b\n\5\4\3\2\t\b\3\2\2\2\t\n\3\2\2\2\n\13\3\2\2\2\13\f\7\4\2\2\f"+
		"\3\3\2\2\2\r\22\7\b\2\2\16\17\7\5\2\2\17\21\7\b\2\2\20\16\3\2\2\2\21\24"+
		"\3\2\2\2\22\20\3\2\2\2\22\23\3\2\2\2\23\5\3\2\2\2\24\22\3\2\2\2\4\t\22";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}