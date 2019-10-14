package org.clever.dynamic.sql.scripting.defaults;

import org.clever.dynamic.sql.builder.SqlSourceBuilder;
import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.scripting.xmltags.DynamicContext;
import org.clever.dynamic.sql.scripting.xmltags.SqlNode;

import java.util.HashMap;

public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode, Class<?> parameterType) {
        this(getSql(rootSqlNode), parameterType);
    }

    public RawSqlSource(String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder();
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    private static String getSql(SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }
}
