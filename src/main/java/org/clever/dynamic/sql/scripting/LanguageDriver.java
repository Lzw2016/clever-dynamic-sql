package org.clever.dynamic.sql.scripting;

import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.parsing.XNode;

public interface LanguageDriver {
    /**
     * 创建一个SqlSource
     *
     * @param script 从XML文件解析的XNode
     */
    SqlSource createSqlSource(XNode script);

    /**
     * 创建一个SqlSource
     *
     * @param script 动态SQL脚本
     */
    SqlSource createSqlSource(String script);
}
