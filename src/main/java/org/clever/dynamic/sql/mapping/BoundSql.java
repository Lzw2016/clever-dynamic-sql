package org.clever.dynamic.sql.mapping;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {

    private final String sql;
    @Getter
    private final List<String> parameterMappings;
    private final Object parameterObject;
    private final Map<String, Object> additionalParameters;

    public BoundSql(String sql, List<String> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
    }

    public String getSql() {
        return sql;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setAdditionalParameter(String name, Object value) {
        additionalParameters.put(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return additionalParameters.get(name);
    }
}
