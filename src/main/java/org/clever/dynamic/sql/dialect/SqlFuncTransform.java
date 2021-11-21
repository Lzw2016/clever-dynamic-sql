package org.clever.dynamic.sql.dialect;

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
     * @param dbType      数据库类型
     * @param sqlFuncNode sql函数
     */
    SqlFuncNode transform(DbType dbType, SqlFuncNode sqlFuncNode);
}
