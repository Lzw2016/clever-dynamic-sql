package org.clever.dynamic.sql.dialect.func;

import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.dialect.SqlFuncDialect;
import org.clever.dynamic.sql.dialect.SqlFuncParam;
import org.clever.dynamic.sql.dialect.SqlFuncTransform;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 22:33 <br/>
 */
public class JoinFuncTransform implements SqlFuncTransform {
    public static final String FUNC_NAME = "join";

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }

    @Override
    public boolean isSupport(DbType dbType) {
        return true;
    }

    @Override
    public SqlFuncDialect transform(DbType dbType, String funcName, List<SqlFuncParam> params) {
        if (params == null || params.size() != 1) {
            throw new IllegalArgumentException("SQL函数" + funcName + "参数错误");
        }
        SqlFuncParam sqlFuncParam = params.get(0);
        if (!(sqlFuncParam.getValue() instanceof Collection)) {
            throw new IllegalArgumentException("SQL函数" + funcName + "参数错误");
        }
        Collection<?> collection = (Collection<?>) sqlFuncParam.getValue();
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("SQL函数" + funcName + "参数错误");
        }
        params.clear();
        StringBuilder sqlFuncLiteral = new StringBuilder();
        int idx = 0;
        LinkedHashMap<String, Object> sqlParams = new LinkedHashMap<>();
        for (Object value : collection) {
            idx++;
            String literal = "id_" + idx;
            sqlParams.put(literal, value);
            if (sqlFuncLiteral.length() > 0) {
                sqlFuncLiteral.append(", ");
            }
            sqlFuncLiteral.append("#{").append(literal).append("}");
        }
        return new SqlFuncDialect(sqlFuncLiteral.toString(), sqlParams);
    }
}
