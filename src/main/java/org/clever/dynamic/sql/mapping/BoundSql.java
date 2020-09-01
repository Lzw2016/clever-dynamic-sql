package org.clever.dynamic.sql.mapping;

import lombok.Getter;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.reflection.property.PropertyTokenizer;
import org.clever.dynamic.sql.utils.ConfigurationUtils;

import java.util.*;

/**
 * 动态SQL脚本信息
 */
public class BoundSql {
    /**
     * 原始参数对象
     */
    @Getter
    private final Object parameterObject;
    /**
     * 生成的SQL语句("?" 参数形式)
     */
    @Getter
    private final String sql;
    /**
     * 生成的SQL语句(":param" 参数形式)
     */
    @Getter
    private final String namedParameterSql;
    /**
     * Sql参数名称列表(有顺序)
     */
    @Getter
    private final List<String> parameterList;
    /**
     * 附加的参数
     */
    private final Map<String, Object> additionalParameters;
    /**
     * 附加的参数的MetaObject包装
     */
    private final MetaObject metaParameters;
    /**
     * Sql参数值列表(有顺序)
     */
    private List<Object> parameterValueList;
    /**
     * Sql参数Map集合
     */
    private Map<String, Object> parameterMap;

    /**
     * @param sql               生成的SQL语句("?" 参数形式)
     * @param namedParameterSql 生成的SQL语句(":param" 参数形式)
     * @param parameterList     Sql参数名称列表(有顺序)
     * @param parameterObject   原始参数对象
     */
    public BoundSql(String sql, String namedParameterSql, List<String> parameterList, Object parameterObject) {
        this.sql = sql;
        this.namedParameterSql = namedParameterSql;
        this.parameterList = parameterList;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = ConfigurationUtils.newMetaObject(additionalParameters);
    }

    /**
     * 参数值列表(有顺序)
     */
    public List<Object> getParameterValueList() {
        if (parameterValueList != null) {
            return parameterValueList;
        }
        initSqlParameter();
        return parameterValueList;
    }

    /**
     * Sql参数Map集合
     */
    public Map<String, Object> getParameterMap() {
        if (parameterMap != null) {
            return parameterMap;
        }
        initSqlParameter();
        return parameterMap;
    }

    private void initSqlParameter() {
        if (parameterList == null || parameterList.isEmpty()) {
            parameterValueList = Collections.emptyList();
            parameterMap = Collections.emptyMap();
            return;
        }
        parameterValueList = new ArrayList<>(parameterList.size());
        parameterMap = new HashMap<>(parameterList.size());
        parameterList.forEach(name -> {
            Object value;
            if (hasAdditionalParameter(name)) {
                value = getAdditionalParameter(name);
            } else if (this.parameterObject == null) {
                value = null;
            } else {
                MetaObject metaObject = ConfigurationUtils.newMetaObject(parameterObject);
                value = metaObject.getValue(name);
            }
            parameterValueList.add(value);
            parameterMap.put(name, value);
        });
    }

    private boolean hasAdditionalParameter(String name) {
        String paramName = new PropertyTokenizer(name).getName();
        return additionalParameters.containsKey(paramName);
    }

    void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

    private Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }
}
