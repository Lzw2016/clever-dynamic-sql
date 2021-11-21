package org.clever.dynamic.sql.node;

import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.dialect.SqlFuncDialectTransform;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicContext {
    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    static {
        // 初始化OGNL表达式环境
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    /**
     * OgnlContext
     */
    private final ContextMap bindings;
    /**
     * SQL语句缓存
     */
    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    /**
     * 自增唯一数字
     */
    private int uniqueNumber = 0;
    /**
     * 表达式参数集合
     */
    private final Set<String> parameterExpressionSet = new LinkedHashSet<>();
    /**
     * 参数集合
     */
    private final Set<String> parameterVar = new LinkedHashSet<>();

    public DynamicContext(Object parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            MetaObject metaObject = MetaObject.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    static final Pattern pattern = Pattern.compile("'@\\{.+?}'");

    public SqlFuncDialectTransform parser(String code, DbType dbType) {
        CharStream charStream = CharStreams.fromString(code);
        SqlFuncLexer lexer = new SqlFuncLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SqlFuncParser parser = new SqlFuncParser(tokenStream);
        ParseTree tree = parser.sqlFunc();
        ParseTreeWalker walker = new ParseTreeWalker();
        List<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(4);
        arr.add(5);
        bindings.put("arr", arr);
        SqlFuncDialectTransform transform = new SqlFuncDialectTransform(dbType, bindings, lexer, tokenStream, parser);
        walker.walk(transform, tree);
        return transform;
    }

    public void appendSql(String sql) {
        // TODO !!!
        Matcher matcher = pattern.matcher(sql);
        StringBuilder newSql = new StringBuilder();
        int start = 0;
        while (matcher.find()) {
            newSql.append(sql, start, matcher.start());
            String code = matcher.group();
            code = code.substring(3, code.length() - 2);
            start = matcher.end();
            SqlFuncDialectTransform transform = parser(code, DbType.SQL_SERVER);
            newSql.append(transform.toSql());
            // bindings.putAll(transform.getParams());
        }
        newSql.append(sql, start, sql.length());
        sqlBuilder.add(newSql.toString());
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    public void addParameterExpression(String expression) {
        if (StringUtils.Instance.isNotBlank(expression)) {
            parameterExpressionSet.add(expression);
        }
    }

    public Set<String> getParameterExpressionSet() {
        return Collections.unmodifiableSet(parameterExpressionSet.stream().filter(str -> !parameterVar.contains(str)).collect(Collectors.toSet()));
    }

    public void addParameterVar(String var) {
        if (StringUtils.Instance.isNotBlank(var)) {
            parameterVar.add(var);
        }
    }

    static class ContextMap extends HashMap<String, Object> {
        private final MetaObject parameterMetaObject;
        @SuppressWarnings("FieldCanBeLocal")
        private final boolean fallbackParameterObject = false;

        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }
            if (parameterMetaObject == null) {
                return null;
            }
            if (fallbackParameterObject && !parameterMetaObject.hasGetter(strKey)) {
                return parameterMetaObject.getOriginalObject();
            } else {
                // issue #61 do not modify the context when reading
                return parameterMetaObject.getValue(strKey);
            }
        }
    }

    static class ContextAccessor implements PropertyAccessor {
        @SuppressWarnings("rawtypes")
        @Override
        public Object getProperty(Map context, Object target, Object name) {
            Map map = (Map) target;
            Object result = map.get(name);
            if (map.containsKey(name) || result != null) {
                return result;
            }
            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setProperty(Map context, Object target, Object name, Object value) {
            Map<Object, Object> map = (Map<Object, Object>) target;
            map.put(name, value);
        }

        @Override
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }
}