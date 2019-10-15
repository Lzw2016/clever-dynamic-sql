package org.clever.dynamic.sql.mapping;

import lombok.Getter;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.reflection.property.PropertyTokenizer;
import org.clever.dynamic.sql.utils.ConfigurationUtils;

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
     * 附加的参数
     */
    private final Map<String, Object> additionalParameters;
    /**
     * 附加的参数的MetaObject包装
     */
    private final MetaObject metaParameters;
    /**
     * 参数值列表(有顺序)
     */
    private List<Object> parameterValueList;

    public BoundSql(String sql, List<String> parameterList, Object parameterObject) {
        this.sql = sql;
        this.parameterList = parameterList;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = ConfigurationUtils.newMetaObject(additionalParameters);
    }

    public boolean hasAdditionalParameter(String name) {
        String paramName = new PropertyTokenizer(name).getName();
        return additionalParameters.containsKey(paramName);
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }

    public List<Object> getParameterValueList() {
        if (parameterValueList != null) {
            return parameterValueList;
        }
        if (parameterList == null || parameterList.isEmpty()) {
            parameterValueList = Collections.emptyList();
            return parameterValueList;
        }
        parameterValueList = new ArrayList<>(parameterList.size());
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
        });
        return parameterValueList;
    }
}
