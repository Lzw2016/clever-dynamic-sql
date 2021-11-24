package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.node.DynamicContext;
import org.clever.dynamic.sql.node.SqlNode;

import java.util.HashMap;

public class RawSqlSource implements SqlSource {
    private final StaticSqlSource sqlSource;

    public RawSqlSource(DbType dbType, SqlNode rootSqlNode) {
        this(dbType, getSql(rootSqlNode));
    }

    public RawSqlSource(DbType dbType, String sql) {
        this.sqlSource = new StaticSqlSource(dbType, sql, new DynamicContext(new HashMap<>()));
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
