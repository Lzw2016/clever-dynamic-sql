lexer grammar SqlFuncLexer;

LPAREN:             '(';
RPAREN:             ')';
COMMA:              ',';
DOT:                '.';
//AT:                 '@';
//LBRACE:             '{';
//RBRACE:             '}';
//COMMA:              ',';

IDENTIFIER:         [a-zA-Z0-9$_]+;
STRING_LITERAL:     ('"' (~["\\\r\n])* '"');
WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);
