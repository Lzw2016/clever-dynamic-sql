package org.clever.dynamic.sql.mapping;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {
    /**
     * 原始参数对象
     */
    @Getter
    private final Object parameterObject;
    /**
     * 生成的SQL语句
     */
    @Getter
    private final String sql;
    /**
     * 参数列表(有顺序)
     */
    @Getter
    private final List<String> parameterList;
    /**
     * parameterMappings
     */
    private final Map<String, Object> parametersMap;

    public BoundSql(String sql, List<String> parameterList, Object parameterObject) {
        this.sql = sql;
        this.parameterList = parameterList;
        this.parameterObject = parameterObject;
        this.parametersMap = new HashMap<>();
    }


    public void setParameter(String name, Object value) {
        parametersMap.put(name, value);
    }

    public Object getParameter(String name) {
        return parametersMap.get(name);
    }
}
