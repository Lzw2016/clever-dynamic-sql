package org.clever.dynamic.sql.scripting.defaults;

import org.clever.dynamic.sql.builder.SqlSourceBuilder;
import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.scripting.xmltags.DynamicContext;
import org.clever.dynamic.sql.scripting.xmltags.SqlNode;

public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        this(getSql(rootSqlNode));
    }

    public RawSqlSource(String sql) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder();
        sqlSource = sqlSourceParser.parse(sql);
    }

    private static String getSql(SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        boundSql.getParameterValueList();
        return boundSql;
    }
}
