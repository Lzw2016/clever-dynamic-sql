package org.clever.dynamic.sql.builder;

import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private final String sql;
    private final List<String> parameterMappings;

    public StaticSqlSource(String sql, List<String> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, parameterMappings, parameterObject);
    }
}
