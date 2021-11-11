package org.clever.dynamic.sql.dialect;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 17:31 <br/>
 */
public interface SqlFuncTransform {
    /**
     * sql函数名称
     */
    String getFuncName();

    /**
     * 是否支持转换
     *
     * @param dbType 数据库类型
     */
    boolean isSupport(DbType dbType);

    /**
     * 函数方言转换
     *
     * @param dbType   数据库类型
     * @param funcName 函数名称
     * @param params   参数信息
     */
    SqlFuncDialect transform(DbType dbType, String funcName, List<SqlFuncParam> params);
}
