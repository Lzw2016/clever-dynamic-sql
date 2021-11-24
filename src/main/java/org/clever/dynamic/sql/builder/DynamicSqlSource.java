package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.node.DynamicContext;
import org.clever.dynamic.sql.node.SqlNode;

public class DynamicSqlSource implements SqlSource {
    private final SqlNode rootSqlNode;
    private final DbType dbType;

    public DynamicSqlSource(DbType dbType, SqlNode rootSqlNode) {
        this.dbType = dbType;
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(parameterObject);
        rootSqlNode.apply(context);
        StaticSqlSource sqlSource = new StaticSqlSource(dbType, context.getSql(), context);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        context.getBindings().forEach(boundSql::setAdditionalParameter);
        context.getParameterExpressionSet().forEach(srt -> boundSql.getParameterExpressionSet().add(srt));
        return boundSql;
    }
}
