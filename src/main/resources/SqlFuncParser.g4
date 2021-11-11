parser grammar SqlFuncParser;

options { tokenVocab=SqlFuncLexer; }


//// @{func_name(aa.ss.m(), "bb", cc, dd, ...)}
//funcDeclaration
//    : IDENTIFIER '(' parameterList? ')'
//    ;
//
//parameterList
//    : parameter (',' parameter)*
//    ;
//
//parameter
//    : STRING_LITERAL
//    | IDENTIFIER ('.' IDENTIFIER)* ('.' IDENTIFIER '(' parameterList? ')')?
//    ;

// 函数参数
parameter
    : STRING_LITERAL
    | sqlFunc
    ;

// sql语言函数名
sqlFunc
    : IDENTIFIER
    ;

// java语言函数名
javaFunc
    : IDENTIFIER ('.' IDENTIFIER ('(' ')'))*
    ;


/*
1.javaFunc(IDENTIFIER, NULL_LITERAL, BOOL_LITERAL, CHAR_LITERAL, DECIMAL_LITERAL, STRING_LITERAL, javaFunc|sqlFunc)
2.sqlFunc(IDENTIFIER, NULL_LITERAL, BOOL_LITERAL, CHAR_LITERAL, DECIMAL_LITERAL, STRING_LITERAL, javaFunc|sqlFunc)
3.funcDeclaration -> IDENTIFIER(javaFunc|sqlFunc)
*/