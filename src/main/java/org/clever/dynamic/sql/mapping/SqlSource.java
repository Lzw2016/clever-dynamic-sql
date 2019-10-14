package org.clever.dynamic.sql.mapping;

public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);
}
