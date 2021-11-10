parser grammar SqlFuncParser;

options { tokenVocab=SqlFuncLexer; }

// @{func_name(aa.ss.m(), "bb", cc, dd, ...)}
funcDeclaration
    : IDENTIFIER '(' parameterList? ')'
    ;

parameterList
    : PARAMETER (',' PARAMETER)*
    ;
