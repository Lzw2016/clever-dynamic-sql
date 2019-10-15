package org.clever.dynamic.sql.builder;

import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private final String sql;
    private final List<String> parameterList;

    public StaticSqlSource(String sql, List<String> parameterList) {
        this.sql = sql;
        this.parameterList = parameterList;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = new BoundSql(sql, parameterList, parameterObject);
        boundSql.getParameterValueList();
        return boundSql;
    }
}
