package org.clever.dynamic.sql.dialect;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParserBaseListener;
import org.clever.dynamic.sql.dialect.exception.SqlFuncTransformAlreadyExists;
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

    /**
     * 注册SqlFuncTransform
     */
    public static void register(SqlFuncTransform sqlFuncTransform) {
        if (sqlFuncTransform == null) {
            return;
        }
        CopyOnWriteArrayList<SqlFuncTransform> transforms = TRANSFORMS_MAP.computeIfAbsent(sqlFuncTransform.getFuncName(), funcName -> new CopyOnWriteArrayList<>());
        if (transforms.stream().anyMatch(transform -> Objects.equals(transform.getClass(), sqlFuncTransform.getClass()))) {
            throw new SqlFuncTransformAlreadyExists("当前SqlFuncTransform类型已存在，class=" + sqlFuncTransform.getClass().getName());
        }
        transforms.add(sqlFuncTransform);
    }

    /**
     * 获取 SqlFuncTransform
     *
     * @param funcName SQL函数名称
     * @param dbType   数据库类型
     */
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

    /**
     * 是否有解析错误
     */
    private boolean hasError = false;
    /**
     * 最外层sql函数
     */
    private SqlFuncNode rootSqlFuncNode;
    /**
     * 解析sql函数的栈容器
     */
    private final Stack<SqlFuncNode> sqlFuncNodeStack = new Stack<>();
    /**
     * 转换后的sql
     */
    private final StringBuilder sqlFuncLiteral = new StringBuilder();
    /**
     * 对应的sql变量参数，形如#{variable}的变量
     */
    @Getter // TODO 改写Getter函数?
    private final LinkedHashMap<String, Object> sqlVariable = new LinkedHashMap<>();

    public String getSqlFuncLiteral() {
        return sqlFuncLiteral.toString();
    }

    /**
     * @param dbType   数据库类型
     * @param ognlRoot ognl表达式的root对象
     */
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
        if (hasError) {
            return;
        }
        String sqlFuncName = ctx.IDENTIFIER().getSymbol().getText();
        SqlFuncNode sqlFuncNode = new SqlFuncNode(sqlFuncName);
        sqlFuncNodeStack.push(sqlFuncNode);
        if (rootSqlFuncNode == null) {
            rootSqlFuncNode = sqlFuncNode;
        }
    }

    @Override
    public void exitSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
        if (hasError) {
            return;
        }
        sqlFuncNodeStack.pop();
//        String sqlFuncName = sqlFuncNameStack.pop();
//        SqlFuncTransform sqlFuncTransform = getTransform(sqlFuncName, dbType);
//        if (sqlFuncTransform == null) {
//            throw new RuntimeException("不支持的SQL函数:" + sqlFuncName);
//        }
//        SqlFuncDialect sqlFuncDialect = sqlFuncTransform.transform(dbType, sqlFuncName, sqlFuncParams);
//        sqlFuncLiteral.append(sqlFuncDialect.getSqlFuncLiteral());
//        for (SqlFuncParam param : sqlFuncParams) {
//            if (param.isVariable()) {
//                sqlVariable.put(param.getName(), param.getValue());
//            }
//        }
//        sqlFuncParams.clear();
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
        if (hasError) {
            return;
        }
        SqlFuncNode sqlFuncNode = sqlFuncNodeStack.peek();
        SqlFuncParser.SqlFuncContext sqlFuncContext = ctx.sqlFunc();
        SqlFuncParser.JavaVarContext javaVarContext = ctx.javaVar();
        SqlFuncParser.JavaFuncContext javaFuncContext = ctx.javaFunc();
        SqlFuncNodeParam param;
        if (sqlFuncContext != null) {
            // sql函数
            String funcName = sqlFuncContext.IDENTIFIER().getSymbol().getText();
            SqlFuncNode sqlFunc = new SqlFuncNode(funcName);
            param = new SqlFuncNodeParam(sqlFunc);
        } else if (javaVarContext != null) {
            // java变量(链式变量取值)

        } else if (javaFuncContext != null) {
            // java函数

        } else {
            // null值 | bool值 | 整数数 | 浮点数 | 字符串值
            String literal = ctx.getText();
            SqlFuncParam sqlFuncParam = new SqlFuncParam(false, literal, null, null);
            param = new SqlFuncNodeParam(sqlFuncParam);
        }
        sqlFuncNode.getParams().add(param);

//        if (ctx.sqlFunc() != null || ctx.javaVar() != null || ctx.javaFunc() != null) {
//            return;
//        }
//        final String sqlParameter = ctx.getText();
//        log.info("sqlParameter      -> {}", sqlParameter);
//        if (ctx.javaVar() != null) {
//            String literal = ctx.javaVar().getText();
//            Object param = OgnlCache.getValue(literal, ognlRoot);
//            SqlFuncParam sqlFuncParam = new SqlFuncParam(true, literal, literal, param);
//            sqlFuncParams.add(sqlFuncParam);
//        }
    }

//    @Override
//    public void exitSqlParameter(SqlFuncParser.SqlParameterContext ctx) {
//        if (hasError) {
//            return;
//        }
//    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        hasError = true;
    }

    @Data
    public static final class SqlFuncNode {
        /**
         * SQL函数名称
         */
        private final String funcName;
        /**
         * SQL函数参数
         */
        private final List<SqlFuncNodeParam> params = new ArrayList<>();

        public SqlFuncNode(String funcName) {
            this.funcName = funcName;
        }
    }

    @Data
    public static final class SqlFuncNodeParam {
        /**
         * 参数类型
         */
        private final NodeChildTypeEnum type;
        /**
         * SQL参数
         */
        private final SqlFuncParam param;
        /**
         * 子函数
         */
        private final SqlFuncNode func;

        public SqlFuncNodeParam(SqlFuncParam param) {
            this.type = NodeChildTypeEnum.SQL_PARAM;
            this.param = param;
            this.func = null;
        }

        public SqlFuncNodeParam(SqlFuncNode func) {
            this.type = NodeChildTypeEnum.CHILD_FUNC;
            this.param = null;
            this.func = func;
        }
    }

    public enum NodeChildTypeEnum {
        /**
         * SQL参数
         */
        SQL_PARAM,
        /**
         * 子函数
         */
        CHILD_FUNC,
    }
}
