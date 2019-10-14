package org.clever.dynamic.sql.mapping;

import lombok.Getter;

import java.util.*;

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
     * 参数名称列表(有顺序)
     */
    @Getter
    private final List<String> parameterList;
    /**
     * 参数 Map 集合
     */
    private final Map<String, Object> parametersMap;
    /**
     * 参数值列表(有顺序)
     */
    private List<Object> parameterValueList;

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

    public List<Object> getParameterValueList() {
        if (parameterValueList == null) {
            if (parameterList == null || parameterList.isEmpty()) {
                parameterValueList = Collections.emptyList();
            } else {
                parameterValueList = new ArrayList<>(parameterList.size());
                parameterList.forEach(name -> parameterValueList.add(parametersMap.get(name)));
            }
        }
        return parameterValueList;
    }
}
