package org.clever.dynamic.sql.mapping;

import java.util.HashMap;
import java.util.Map;

public class BoundSql {

    private final String sql;
    //  private final List<ParameterMapping> parameterMappings;
    private final Object parameterObject;
    private final Map<String, Object> additionalParameters;
    //  private final MetaObject metaParameters;

    public BoundSql(String sql, Object parameterObject) {
        this.sql = sql;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
    }

    public String getSql() {
        return sql;
    }

    public Object getParameterObject() {
        return parameterObject;
    }
}
