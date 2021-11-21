package org.clever.dynamic.sql.dialect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParserBaseListener;
import org.clever.dynamic.sql.dialect.exception.ParseSqlFuncException;
import org.clever.dynamic.sql.dialect.exception.SqlFuncTransformAlreadyExistsException;
import org.clever.dynamic.sql.ognl.OgnlCache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 17:02 <br/>
 */
@Slf4j
public class SqlFuncDialectTransform extends SqlFuncParserBaseListener {
    /**
     * {@code Map<FuncName, List<SqlFuncTransform>>}
     */
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
            throw new SqlFuncTransformAlreadyExistsException("当前SqlFuncTransform类型已存在，class=" + sqlFuncTransform.getClass().getName());
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
    @Getter
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
     * 转换后的sql函数代码
     */
    public String getSqlFuncLiteral() {
        StringBuilder sqlFuncLiteral = new StringBuilder();


        return sqlFuncLiteral.toString();
    }

//    private String getSqlFuncLiteral(SqlFuncNode sqlFunc) {
//        StringBuilder sqlFuncLiteral = new StringBuilder();
//
//        String sqlFuncName = sqlFunc.getFuncName();
//        SqlFuncTransform sqlFuncTransform = getTransform(sqlFuncName, dbType);
//        if (sqlFuncTransform == null) {
//
//        }
//
//
//        SqlFuncDialect sqlFuncDialect = sqlFuncTransform.transform(dbType, sqlFuncName, sqlFuncParams);
//        sqlFuncLiteral.append(sqlFuncDialect.getSqlFuncLiteral());
//        for (SqlFuncParam param : sqlFuncParams) {
//            if (param.isVariable()) {
//                sqlVariable.put(param.getName(), param.getValue());
//            }
//        }
//        sqlFuncParams.clear();
//    }


    private SqlFuncNode transform(SqlFuncNode sqlFunc) {
        String sqlFuncName = sqlFunc.getFuncName();
        SqlFuncTransform sqlFuncTransform = getTransform(sqlFuncName, dbType);
        if (sqlFuncTransform == null) {
            return sqlFunc.copy();
        }

        return null;
    }

    /**
     * sql变量
     */
    public LinkedHashMap<String, Object> getSqlVariable() {
        if (hasError) {
            throw new ParseSqlFuncException("解析SQL函数失败");
        }
        return getSqlVariable(rootSqlFuncNode);
    }

    /**
     * 递归读取sql变量
     */
    private LinkedHashMap<String, Object> getSqlVariable(SqlFuncNode sqlFunc) {
        LinkedHashMap<String, Object> sqlVariable = new LinkedHashMap<>();
        for (SqlFuncNodeParam param : sqlFunc.getParams()) {
            if (SqlFuncNodeParamEnum.SQL_FUNC.equals(param.getType())) {
                SqlFuncNode childSqlFunc = param.getFunc();
                sqlVariable.putAll(getSqlVariable(childSqlFunc));
            } else {
                SqlFuncParam sqlFuncParam = param.getParam();
                if (sqlFuncParam.isVariable()) {
                    String name = sqlFuncParam.getName();
                    Object value = sqlFuncParam.getValue();
                    sqlVariable.put(name, value);
                }
            }
        }
        return sqlVariable;
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

    @Override
    public void enterSqlFunc(SqlFuncParser.SqlFuncContext ctx) {
        if (hasError) {
            return;
        }
        String sqlFuncName = ctx.IDENTIFIER().getSymbol().getText();
        // 上级函数存在 & 上级函数的最后一个参数是SqlFuncNode
        if (!sqlFuncNodeStack.isEmpty()) {
            SqlFuncNode parentSqlFuncNode = sqlFuncNodeStack.peek();
            if (parentSqlFuncNode != null && !parentSqlFuncNode.getParams().isEmpty()) {
                SqlFuncNodeParam param = parentSqlFuncNode.getParams().get(parentSqlFuncNode.getParams().size() - 1);
                if (SqlFuncNodeParamEnum.SQL_FUNC.equals(param.getType())
                        && param.getFunc() != null
                        && Objects.equals(sqlFuncName, param.getFunc().getFuncName())) {
                    sqlFuncNodeStack.push(param.getFunc());
                } else {
                    throw new ParseSqlFuncException("解析SQL函数异常：sqlFuncName=" + sqlFuncName + " | param=" + param);
                }
                return;
            }
        }
        // 当前SQL函数入栈
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
    }

    @Override
    public void enterSqlParameter(SqlFuncParser.SqlParameterContext ctx) {
        if (hasError) {
            return;
        }
        if (sqlFuncNodeStack.isEmpty()) {
            throw new ParseSqlFuncException("解析SQL函数失败");
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
            String literal = javaVarContext.getText();
            Object value = OgnlCache.getValue(literal, ognlRoot);
            SqlFuncParam sqlFuncParam = new SqlFuncParam(true, literal, literal, value);
            param = new SqlFuncNodeParam(sqlFuncParam);
        } else if (javaFuncContext != null) {
            // java函数
            String literal = javaFuncContext.getText();
            Object value = OgnlCache.getValue(literal, ognlRoot);
            SqlFuncParam sqlFuncParam = new SqlFuncParam(true, literal, literal, value);
            param = new SqlFuncNodeParam(sqlFuncParam);
        } else {
            // null值 | bool值 | 整数数 | 浮点数 | 字符串值
            String literal = ctx.getText();
            SqlFuncParam sqlFuncParam = new SqlFuncParam(false, literal, null, null);
            param = new SqlFuncNodeParam(sqlFuncParam);
        }
        sqlFuncNode.getParams().add(param);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        hasError = true;
    }
}
