package org.clever.dynamic.sql.mapping;

import org.clever.dynamic.sql.builder.SqlSourceBuilder;
import org.clever.dynamic.sql.scripting.xmltags.DynamicContext;
import org.clever.dynamic.sql.scripting.xmltags.SqlNode;

public class DynamicSqlSource implements SqlSource {

    private final SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        context.getBindings().forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
