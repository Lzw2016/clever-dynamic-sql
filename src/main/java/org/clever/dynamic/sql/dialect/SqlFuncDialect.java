package org.clever.dynamic.sql.dialect;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 20:28 <br/>
 */
@Data
public class SqlFuncDialect {
    /**
     * SQL函数字面值
     */
    private final String sqlFuncLiteral;
    /**
     * sql参数
     */
    private final LinkedHashMap<String, Object> params;

    public SqlFuncDialect(String sqlFuncLiteral, LinkedHashMap<String, Object> params) {
        this.sqlFuncLiteral = sqlFuncLiteral;
        this.params = params;
    }
}
