package org.clever.dynamic.sql.builder;

import org.apache.commons.lang3.StringUtils;
import org.clever.dynamic.sql.exceptions.BuilderException;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.parsing.GenericTokenParser;
import org.clever.dynamic.sql.parsing.TokenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlSourceBuilder extends BaseBuilder {

    private static final String PARAMETER_PROPERTIES = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder() {
        super();
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql, handler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private final List<String> parameterMappings = new ArrayList<>();

        public ParameterMappingTokenHandler() {
        }

        public List<String> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private String buildParameterMapping(String content) {
            Map<String, String> propertiesMap = parseParameterMapping(content);
            String property = null;
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if ("property".equals(name)) {
                    // Do Nothing
                    property = value;
                } else if ("expression".equals(name)) {
                    throw new BuilderException("Expression based parameters are not supported yet");
                } else {
                    throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + PARAMETER_PROPERTIES);
                }
            }
            if (StringUtils.isBlank(property)) {
                throw new BuilderException("parameters is not empty");
            }
            return property;
        }

        private Map<String, String> parseParameterMapping(String content) {
            try {
                return new ParameterExpression(content);
            } catch (BuilderException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
            }
        }
    }
}
