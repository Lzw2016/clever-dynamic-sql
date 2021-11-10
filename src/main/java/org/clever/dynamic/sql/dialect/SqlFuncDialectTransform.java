package org.clever.dynamic.sql.dialect;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
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

    public SqlFuncDialectTransform(SqlFuncLexer lexer, CommonTokenStream tokenStream, SqlFuncParser sqlFuncParser) {
        this.lexer = lexer;
        this.tokenStream = tokenStream;
        this.sqlFuncParser = sqlFuncParser;
    }

    @Override
    public void enterFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx) {
        final String funcName = ctx.IDENTIFIER().getSymbol().getText();
        log.info("funcName -> {}", funcName);
    }

    @Override
    public void exitFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx) {
        super.exitFuncDeclaration(ctx);
    }

    @Override
    public void enterParameterList(SqlFuncParser.ParameterListContext ctx) {
        super.enterParameterList(ctx);
    }

    @Override
    public void exitParameterList(SqlFuncParser.ParameterListContext ctx) {
        super.exitParameterList(ctx);
    }

    @Override
    public void enterParameter(SqlFuncParser.ParameterContext ctx) {
        final String paramName = ctx.getText();
        log.info("paramName -> {}", paramName);
    }

    @Override
    public void exitParameter(SqlFuncParser.ParameterContext ctx) {
        super.exitParameter(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
