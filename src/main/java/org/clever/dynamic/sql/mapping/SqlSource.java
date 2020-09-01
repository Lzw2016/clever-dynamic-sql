package org.clever.dynamic.sql.mapping;

/**
 * SQL脚本
 */
public interface SqlSource {

    /**
     * 获取SQL脚本信息
     *
     * @param parameterObject SQL参数
     */
    BoundSql getBoundSql(Object parameterObject);
}
