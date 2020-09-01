package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.node.DynamicContext;
import org.clever.dynamic.sql.node.SqlNode;

public class DynamicSqlSource implements SqlSource {
    private final SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(context);
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        context.getBindings().forEach(boundSql::setAdditionalParameter);
        context.getParameterExpressionSet().forEach(srt -> boundSql.getParameterExpressionSet().add(srt));
        return boundSql;
    }
}
