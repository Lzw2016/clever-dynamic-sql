package org.clever.dynamic.sql.builder;

import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;

public class StaticSqlSource implements SqlSource {

    private final String sql;
//    private final List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql) {
        this.sql = sql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, parameterObject);
    }
}
