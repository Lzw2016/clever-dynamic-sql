package org.clever.dynamic.sql.dialect;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 17:46 <br/>
 */
@Slf4j
public class ToDateFuncTransform implements SqlFuncTransform {
    public static final String FUNC_NAME = "to_date";

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
        LinkedHashMap<String, Object> sqlParams = new LinkedHashMap<>();
        sqlParams.put(sqlFuncParam.getName(), sqlFuncParam.getValue());
        switch (dbType) {
            case MYSQL:
                return new SqlFuncDialect(String.format("#{%s}", sqlFuncParam.getLiteral()), sqlParams);
            case ORACLE:
                return new SqlFuncDialect(String.format("TO_DATE(#{%s}, 'YYYY-MM-DD')", sqlFuncParam.getLiteral()), sqlParams);
            case SQL_SERVER:
                return new SqlFuncDialect(String.format("CONVERT(datetime, #{%s})", sqlFuncParam.getLiteral()), sqlParams);
            default:
                throw new RuntimeException("不支持的数据库:" + dbType);
        }
    }
}
