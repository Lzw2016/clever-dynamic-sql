package org.clever.dynamic.sql.builder;


import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.node.DynamicContext;
import org.clever.dynamic.sql.parsing.GenericTokenParser;
import org.clever.dynamic.sql.utils.ParseSqlFuncUtils;

import java.util.LinkedHashMap;

public class StaticSqlSource implements SqlSource {
    private final DbType dbType;
    private final DynamicContext context;
    private final String originalSql;

    public StaticSqlSource(DbType dbType, String originalSql, DynamicContext context) {
        this.dbType = dbType;
        this.originalSql = originalSql;
        this.context = context;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        String sqlDialect = originalSql;
        LinkedHashMap<String, Object> sqlVariable = new LinkedHashMap<>();
        if (ParseSqlFuncUtils.needParse(originalSql)) {
            sqlDialect = ParseSqlFuncUtils.parseSqlFunc(dbType, originalSql, parameterObject, sqlVariable);
        }
        // 普通sql(参数占位符 ?)
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        final String sql = parser.parse(sqlDialect);
        // namedParameterSql(参数占位符 :parameterName)
        handler = new ParameterMappingTokenHandler() {
            @Override
            public String handleToken(String content) {
                context.addParameterExpression(content);
                String parameterName = buildParameterMapping(content);
                parameterList.add(parameterName);
                return ":" + parameterName;
            }
        };
        parser = new GenericTokenParser("#{", "}", handler);
        final String namedParameterSql = parser.parse(sqlDialect);
        BoundSql boundSql = new BoundSql(sql, namedParameterSql, handler.getParameterList(), parameterObject);
        sqlVariable.forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
