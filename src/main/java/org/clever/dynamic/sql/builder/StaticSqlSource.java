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

//    private final String sql;
//    private final String namedParameterSql;
//    private final List<String> parameterList;

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
            // TODO 参数顺序问题
            sqlDialect = ParseSqlFuncUtils.parseSqlFunc(dbType, originalSql, parameterObject, sqlVariable);
        }
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(sqlDialect);
        parser = new GenericTokenParser("#{", "}", new ParameterMappingTokenHandler() {
            @Override
            public String handleToken(String content) {
                context.addParameterExpression(content);
                String parameterName = buildParameterMapping(content);
                parameterList.add(parameterName);
                return ":" + parameterName;
            }
        });
        String namedParameterSql = parser.parse(sqlDialect);
        BoundSql boundSql = new BoundSql(sql, namedParameterSql, handler.getParameterList(), parameterObject);
        sqlVariable.forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
