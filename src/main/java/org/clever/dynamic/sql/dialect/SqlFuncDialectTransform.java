package org.clever.dynamic.sql.dialect;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParserBaseListener;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 17:02 <br/>
 */
@Slf4j
public class SqlFuncDialectTransform extends SqlFuncParserBaseListener {
    private final SqlFuncLexer lexer;
    private final CommonTokenStream tokenStream;
    private final SqlFuncParser sqlFuncParser;
    private boolean inJavaFunc = false;

    public SqlFuncDialectTransform(SqlFuncLexer lexer, CommonTokenStream tokenStream, SqlFuncParser sqlFuncParser) {
        this.lexer = lexer;
        this.tokenStream = tokenStream;
        this.sqlFuncParser = sqlFuncParser;
    }

    @Override
    public void enterJavaFunc(SqlFuncParser.JavaFuncContext ctx) {
        inJavaFunc = true;
        final String javaFunc = ctx.getText();
        log.info("javaFunc          -> {}", javaFunc);
    }

    @Override
    public void exitJavaFunc(SqlFuncParser.JavaFuncContext ctx) {
        inJavaFunc = false;
    }

//    @Override
//    public void enterJavaParameterList(SqlFuncParser.JavaParameterListContext ctx) {
//        final String javaParameterList = ctx.getText();
//        log.info("javaParameterList -> {}", javaParameterList);
//    }
//
//    @Override
//    public void exitJavaParameterList(SqlFuncParser.JavaParameterListContext ctx) {
//    }

    @Override
    public void enterJavaParameter(SqlFuncParser.JavaParameterContext ctx) {
        if (inJavaFunc) {
            return;
        }
        if (ctx.javaVar() != null || ctx.javaFunc() != null) {
            return;
        }
        final String javaParameter = ctx.getText();
        log.info("javaParameter     -> {}", javaParameter);
    }

    @Override
    public void exitJavaParameter(SqlFuncParser.JavaParameterContext ctx) {
    }

    @Override
    public void enterJavaVar(SqlFuncParser.JavaVarContext ctx) {
        if (inJavaFunc) {
            return;
        }
        final String javaVar = ctx.getText();
        log.info("javaVar           -> {}", javaVar);
    }

    @Override
    public void exitJavaVar(SqlFuncParser.JavaVarContext ctx) {
    }

    @Override
    public void enterSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
        final String sqlFunc = ctx.getText();
        log.info("sqlFunc           -> {}", sqlFunc);
    }

    @Override
    public void exitSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
    }

//    @Override
//    public void enterSqlParameterList(SqlFuncParser.SqlParameterListContext ctx) {
//        final String sqlParameterList = ctx.getText();
//        log.info("sqlParameterList  -> {}", sqlParameterList);
//    }
//
//    @Override
//    public void exitSqlParameterList(SqlFuncParser.SqlParameterListContext ctx) {
//    }

    @Override
    public void enterSqlParameter(SqlFuncParser.SqlParameterContext ctx) {
        if (ctx.sqlFunc() != null || ctx.javaVar() != null || ctx.javaFunc() != null) {
            return;
        }
        final String sqlParameter = ctx.getText();
        log.info("sqlParameter      -> {}", sqlParameter);
    }

    @Override
    public void exitSqlParameter(SqlFuncParser.SqlParameterContext ctx) {
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
