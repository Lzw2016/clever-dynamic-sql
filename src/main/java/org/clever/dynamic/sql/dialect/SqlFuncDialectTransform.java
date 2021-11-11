package org.clever.dynamic.sql.dialect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParserBaseListener;
import org.clever.dynamic.sql.ognl.OgnlCache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 17:02 <br/>
 */
@Slf4j
public class SqlFuncDialectTransform extends SqlFuncParserBaseListener {
    private static final ConcurrentMap<String, CopyOnWriteArrayList<SqlFuncTransform>> TRANSFORMS_MAP = new ConcurrentHashMap<>();

    public static void register(SqlFuncTransform sqlFuncTransform) {
        CopyOnWriteArrayList<SqlFuncTransform> transforms = TRANSFORMS_MAP.computeIfAbsent(sqlFuncTransform.getFuncName(), funcName -> new CopyOnWriteArrayList<>());
        if (transforms.stream().anyMatch(transform -> Objects.equals(transform.getClass(), sqlFuncTransform.getClass()))) {
            return;
        }
        transforms.add(sqlFuncTransform);
    }

    public static SqlFuncTransform getTransform(String funcName, DbType dbType) {
        CopyOnWriteArrayList<SqlFuncTransform> transforms = TRANSFORMS_MAP.get(funcName);
        if (transforms == null || transforms.isEmpty()) {
            return null;
        }
        for (SqlFuncTransform transform : transforms) {
            if (transform.isSupport(dbType)) {
                return transform;
            }
        }
        return null;
    }

    private final DbType dbType;
    private final Map<String, Object> ognlRoot;
    private final SqlFuncLexer lexer;
    private final CommonTokenStream tokenStream;
    private final SqlFuncParser sqlFuncParser;

    private final Stack<String> sqlFuncNameStack = new Stack<>();
    private final List<SqlFuncParam> sqlFuncParams = new Stack<>();

    /**
     * 转换后的sql
     */
    private final StringBuilder sqlFuncLiteral = new StringBuilder();
    /**
     * 对应的sql参数
     */
    @Getter
    private final LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    public String getSqlFuncLiteral() {
        return sqlFuncLiteral.toString();
    }

    public SqlFuncDialectTransform(DbType dbType, Map<String, Object> ognlRoot, SqlFuncLexer lexer, CommonTokenStream tokenStream, SqlFuncParser sqlFuncParser) {
        this.dbType = dbType;
        this.ognlRoot = ognlRoot;
        this.lexer = lexer;
        this.tokenStream = tokenStream;
        this.sqlFuncParser = sqlFuncParser;
    }

//    @Override
//    public void enterJavaFunc(SqlFuncParser.JavaFuncContext ctx) {
//        final String javaFunc = ctx.getText();
//        log.info("javaFunc          -> {}", javaFunc);
//    }
//
//    @Override
//    public void exitJavaFunc(SqlFuncParser.JavaFuncContext ctx) {
//    }

//    @Override
//    public void enterJavaParameterList(SqlFuncParser.JavaParameterListContext ctx) {
//        final String javaParameterList = ctx.getText();
//        log.info("javaParameterList -> {}", javaParameterList);
//    }
//
//    @Override
//    public void exitJavaParameterList(SqlFuncParser.JavaParameterListContext ctx) {
//    }

//    @Override
//    public void enterJavaParameter(SqlFuncParser.JavaParameterContext ctx) {
//        if (ctx.javaVar() != null || ctx.javaFunc() != null) {
//            return;
//        }
//        final String javaParameter = ctx.getText();
//        log.info("javaParameter     -> {}", javaParameter);
//    }

//    @Override
//    public void exitJavaParameter(SqlFuncParser.JavaParameterContext ctx) {
//    }

//    @Override
//    public void enterJavaVar(SqlFuncParser.JavaVarContext ctx) {
//        final String javaVar = ctx.getText();
//        log.info("javaVar           -> {}", javaVar);
//    }

//    @Override
//    public void exitJavaVar(SqlFuncParser.JavaVarContext ctx) {
//    }

    @Override
    public void enterSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
        String sqlFuncName = ctx.IDENTIFIER().getSymbol().getText();
        sqlFuncNameStack.push(sqlFuncName);
    }

    @Override
    public void exitSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
        String sqlFuncName = sqlFuncNameStack.pop();
        SqlFuncTransform sqlFuncTransform = getTransform(sqlFuncName, dbType);
        if (sqlFuncTransform == null) {
            throw new RuntimeException("不支持的SQL函数:" + sqlFuncName);
        }
        SqlFuncDialect sqlFuncDialect = sqlFuncTransform.transform(dbType, sqlFuncName, sqlFuncParams);
        sqlFuncLiteral.append(sqlFuncDialect.getSqlFuncLiteral());
        for (SqlFuncParam param : sqlFuncParams) {
            if (param.isVariable()) {
                params.put(param.getName(), param.getValue());
            }
        }
        sqlFuncParams.clear();
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
//        if (ctx.sqlFunc() != null || ctx.javaVar() != null || ctx.javaFunc() != null) {
//            return;
//        }
//        final String sqlParameter = ctx.getText();
//        log.info("sqlParameter      -> {}", sqlParameter);

        if (ctx.javaVar() != null) {
            String literal = ctx.javaVar().getText();
            Object param = OgnlCache.getValue(literal, ognlRoot);
            SqlFuncParam sqlFuncParam = new SqlFuncParam(literal, literal, param, true);
            sqlFuncParams.add(sqlFuncParam);
        }
    }

//    @Override
//    public void exitSqlParameter(SqlFuncParser.SqlParameterContext ctx) {
//    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
