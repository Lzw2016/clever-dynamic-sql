//package org.clever.dynamic.sql.scripting;
//
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//import org.apache.ibatis.parsing.XNode;
//import org.apache.ibatis.session.Configuration;
//
//public interface LanguageDriver {
//
//    /**
//     * Creates a {@link ParameterHandler} that passes the actual parameters to the the JDBC statement.
//     *
//     * @param mappedStatement The mapped statement that is being executed
//     * @param parameterObject The input parameter object (can be null)
//     * @param boundSql        The resulting SQL once the dynamic language has been executed.
//     */
//    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
//
//    /**
//     * Creates an {@link SqlSource} that will hold the statement read from a mapper xml file.
//     * It is called during startup, when the mapped statement is read from a class or an xml file.
//     *
//     * @param configuration The MyBatis configuration
//     * @param script        XNode parsed from a XML file
//     * @param parameterType input parameter type got from a mapper method or specified in the parameterType xml attribute. Can be null.
//     */
//    SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
//
//    /**
//     * Creates an {@link SqlSource} that will hold the statement read from an annotation.
//     * It is called during startup, when the mapped statement is read from a class or an xml file.
//     *
//     * @param configuration The MyBatis configuration
//     * @param script        The content of the annotation
//     * @param parameterType input parameter type got from a mapper method or specified in the parameterType xml attribute. Can be null.
//     */
//    SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);
//
//}
