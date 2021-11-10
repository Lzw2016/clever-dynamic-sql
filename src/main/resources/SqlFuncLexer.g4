lexer grammar SqlFuncLexer;

LPAREN:             '(';
RPAREN:             ')';
COMMA:              ',';
//AT:                 '@';
//LBRACE:             '{';
//RBRACE:             '}';
//COMMA:              ',';
//DOT:                '.';

IDENTIFIER:         [a-zA-Z0-9$_];
STRING_LITERAL:     ('"' (~["\\\r\n])* '"') | '""';
PARAMETER:          STRING_LITERAL | [a-zA-Z0-9$_()\\.]+;
