package org.clever.dynamic.sql.builder;

import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.parsing.GenericTokenParser;
import org.clever.dynamic.sql.parsing.TokenHandler;

import java.util.Map;

public class SqlSourceBuilder extends BaseBuilder {

    private static final String PARAMETER_PROPERTIES = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder() {
        super();
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql);
    }

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        //        private List<ParameterMapping> parameterMappings = new ArrayList<>();
        private Class<?> parameterType;
//        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Class<?> parameterType, Map<String, Object> additionalParameters) {
            super();
            this.parameterType = parameterType;
        }

//        public List<ParameterMapping> getParameterMappings() {
//            return parameterMappings;
//        }

        @Override
        public String handleToken(String content) {
//            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

//        private ParameterMapping buildParameterMapping(String content) {
//            Map<String, String> propertiesMap = parseParameterMapping(content);
//            String property = propertiesMap.get("property");
//            Class<?> propertyType;
//            if (metaParameters.hasGetter(property)) { // issue #448 get type from additional params
//                propertyType = metaParameters.getGetterType(property);
//            } else if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
//                propertyType = parameterType;
//            } else if (JdbcType.CURSOR.name().equals(propertiesMap.get("jdbcType"))) {
//                propertyType = java.sql.ResultSet.class;
//            } else if (property == null || Map.class.isAssignableFrom(parameterType)) {
//                propertyType = Object.class;
//            } else {
//                MetaClass metaClass = MetaClass.forClass(parameterType, configuration.getReflectorFactory());
//                if (metaClass.hasGetter(property)) {
//                    propertyType = metaClass.getGetterType(property);
//                } else {
//                    propertyType = Object.class;
//                }
//            }
//            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
//            Class<?> javaType = propertyType;
//            String typeHandlerAlias = null;
//            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
//                String name = entry.getKey();
//                String value = entry.getValue();
//                if ("javaType".equals(name)) {
//                    javaType = resolveClass(value);
//                    builder.javaType(javaType);
//                } else if ("jdbcType".equals(name)) {
//                    builder.jdbcType(resolveJdbcType(value));
//                } else if ("mode".equals(name)) {
//                    builder.mode(resolveParameterMode(value));
//                } else if ("numericScale".equals(name)) {
//                    builder.numericScale(Integer.valueOf(value));
//                } else if ("resultMap".equals(name)) {
//                    builder.resultMapId(value);
//                } else if ("typeHandler".equals(name)) {
//                    typeHandlerAlias = value;
//                } else if ("jdbcTypeName".equals(name)) {
//                    builder.jdbcTypeName(value);
//                } else if ("property".equals(name)) {
//                    // Do Nothing
//                } else if ("expression".equals(name)) {
//                    throw new BuilderException("Expression based parameters are not supported yet");
//                } else {
//                    throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + PARAMETER_PROPERTIES);
//                }
//            }
//            if (typeHandlerAlias != null) {
//                builder.typeHandler(resolveTypeHandler(javaType, typeHandlerAlias));
//            }
//            return builder.build();
//        }

//        private Map<String, String> parseParameterMapping(String content) {
//            try {
//                return new ParameterExpression(content);
//            } catch (BuilderException ex) {
//                throw ex;
//            } catch (Exception ex) {
//                throw new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
//            }
//        }
    }
}
