package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.node.DynamicContext;
import org.clever.dynamic.sql.node.SqlNode;

public class RawSqlSource implements SqlSource {
    private final SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        this(getSql(rootSqlNode));
    }

    public RawSqlSource(String sql) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(null);
        sqlSource = sqlSourceParser.parse(sql);
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
