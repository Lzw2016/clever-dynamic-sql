// Generated from D:/SourceCode/clever/clever-dynamic-sql/src/main/resources\SqlFuncParser.g4 by ANTLR 4.9.2
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
		LPAREN=1, RPAREN=2, COMMA=3, DOT=4, IDENTIFIER=5, STRING_LITERAL=6, WS=7;
	public static final int
		RULE_funcDeclaration = 0, RULE_parameterList = 1, RULE_parameter = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"funcDeclaration", "parameterList", "parameter"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "','", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LPAREN", "RPAREN", "COMMA", "DOT", "IDENTIFIER", "STRING_LITERAL", 
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
			setState(6);
			match(IDENTIFIER);
			setState(7);
			match(LPAREN);
			setState(9);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER || _la==STRING_LITERAL) {
				{
				setState(8);
				parameterList();
				}
			}

			setState(11);
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
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
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
			setState(13);
			parameter();
			setState(18);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(14);
				match(COMMA);
				setState(15);
				parameter();
				}
				}
				setState(20);
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

	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(SqlFuncParser.STRING_LITERAL, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(SqlFuncParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SqlFuncParser.IDENTIFIER, i);
		}
		public List<TerminalNode> DOT() { return getTokens(SqlFuncParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SqlFuncParser.DOT, i);
		}
		public TerminalNode LPAREN() { return getToken(SqlFuncParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(SqlFuncParser.RPAREN, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlFuncParserListener ) ((SqlFuncParserListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlFuncParserVisitor ) return ((SqlFuncParserVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_parameter);
		int _la;
		try {
			int _alt;
			setState(39);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(21);
				match(STRING_LITERAL);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(22);
				match(IDENTIFIER);
				setState(27);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(23);
						match(DOT);
						setState(24);
						match(IDENTIFIER);
						}
						} 
					}
					setState(29);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(30);
					match(DOT);
					setState(31);
					match(IDENTIFIER);
					setState(32);
					match(LPAREN);
					setState(34);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==IDENTIFIER || _la==STRING_LITERAL) {
						{
						setState(33);
						parameterList();
						}
					}

					setState(36);
					match(RPAREN);
					}
				}

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\t,\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\5\2\f\n\2\3\2\3\2\3\3\3\3\3\3\7\3\23\n\3\f\3\16"+
		"\3\26\13\3\3\4\3\4\3\4\3\4\7\4\34\n\4\f\4\16\4\37\13\4\3\4\3\4\3\4\3\4"+
		"\5\4%\n\4\3\4\5\4(\n\4\5\4*\n\4\3\4\2\2\5\2\4\6\2\2\2.\2\b\3\2\2\2\4\17"+
		"\3\2\2\2\6)\3\2\2\2\b\t\7\7\2\2\t\13\7\3\2\2\n\f\5\4\3\2\13\n\3\2\2\2"+
		"\13\f\3\2\2\2\f\r\3\2\2\2\r\16\7\4\2\2\16\3\3\2\2\2\17\24\5\6\4\2\20\21"+
		"\7\5\2\2\21\23\5\6\4\2\22\20\3\2\2\2\23\26\3\2\2\2\24\22\3\2\2\2\24\25"+
		"\3\2\2\2\25\5\3\2\2\2\26\24\3\2\2\2\27*\7\b\2\2\30\35\7\7\2\2\31\32\7"+
		"\6\2\2\32\34\7\7\2\2\33\31\3\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\35\36\3"+
		"\2\2\2\36\'\3\2\2\2\37\35\3\2\2\2 !\7\6\2\2!\"\7\7\2\2\"$\7\3\2\2#%\5"+
		"\4\3\2$#\3\2\2\2$%\3\2\2\2%&\3\2\2\2&(\7\4\2\2\' \3\2\2\2\'(\3\2\2\2("+
		"*\3\2\2\2)\27\3\2\2\2)\30\3\2\2\2*\7\3\2\2\2\b\13\24\35$\')";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}