package org.clever.dynamic.sql.scripting.xmltags;

import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import org.apache.commons.lang3.StringUtils;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.utils.ConfigurationUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DynamicContext {
    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    static {
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    private final ContextMap bindings;
    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    private int uniqueNumber = 0;
    private final Set<String> parameterExpressionSet = new LinkedHashSet<>();
    private final Set<String> parameterVar = new LinkedHashSet<>();

    public DynamicContext(Object parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            MetaObject metaObject = ConfigurationUtils.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    public void addParameterExpression(String expression) {
        if (StringUtils.isNotBlank(expression)) {
            parameterExpressionSet.add(expression);
        }
    }

    public Set<String> getParameterExpressionSet() {
        return Collections.unmodifiableSet(parameterExpressionSet.stream().filter(str -> !parameterVar.contains(str)).collect(Collectors.toSet()));
    }

    public void addParameterVar(String var) {
        if (StringUtils.isNotBlank(var)) {
            parameterVar.add(var);
        }
    }

    static class ContextMap extends HashMap<String, Object> {
        private final MetaObject parameterMetaObject;
        @SuppressWarnings("FieldCanBeLocal")
        private final boolean fallbackParameterObject = false;

        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }
            if (parameterMetaObject == null) {
                return null;
            }
            if (fallbackParameterObject && !parameterMetaObject.hasGetter(strKey)) {
                return parameterMetaObject.getOriginalObject();
            } else {
                // issue #61 do not modify the context when reading
                return parameterMetaObject.getValue(strKey);
            }
        }
    }

    static class ContextAccessor implements PropertyAccessor {
        @Override
        public Object getProperty(Map context, Object target, Object name) {
            Map map = (Map) target;

            Object result = map.get(name);
            if (map.containsKey(name) || result != null) {
                return result;
            }

            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setProperty(Map context, Object target, Object name, Object value) {
            Map<Object, Object> map = (Map<Object, Object>) target;
            map.put(name, value);
        }

        @Override
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }
}