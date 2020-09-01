package org.clever.dynamic.sql.builder;

import org.clever.dynamic.sql.BoundSql;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
