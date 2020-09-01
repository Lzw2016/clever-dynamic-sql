package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;

import java.util.List;

public class StaticSqlSource implements SqlSource {
    private final String sql;
    private final String namedParameterSql;
    private final List<String> parameterList;

    public StaticSqlSource(String sql, String namedParameterSql, List<String> parameterList) {
        this.sql = sql;
        this.namedParameterSql = namedParameterSql;
        this.parameterList = parameterList;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, namedParameterSql, parameterList, parameterObject);
    }
}
