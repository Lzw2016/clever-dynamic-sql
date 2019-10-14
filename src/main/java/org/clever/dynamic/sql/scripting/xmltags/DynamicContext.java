package org.clever.dynamic.sql.scripting.xmltags;

import lombok.Getter;
import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import org.clever.dynamic.sql.exceptions.BuilderException;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class DynamicContext {
    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    static {
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    private final Map<?, ?> parameterMap;
    private final ContextMap bindings;
    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    private int uniqueNumber = 0;

    public DynamicContext(Object parameterObject) {
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            bindings = new ContextMap(parameterObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        if (parameterObject instanceof Map) {
            parameterMap = (Map<?, ?>) parameterObject;
        } else {
            parameterMap = null;
        }
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        if (parameterMap != null && parameterMap.containsKey(name)) {
            throw new BuilderException("parameter bind error: sqlParameter “" + name + "” invalid，Please use other parameter names!");
        }
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

    static class ContextMap extends HashMap<String, Object> {
        @Getter
        private final Object parameterMetaObject;

        public ContextMap(Object parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            return super.get(key);
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