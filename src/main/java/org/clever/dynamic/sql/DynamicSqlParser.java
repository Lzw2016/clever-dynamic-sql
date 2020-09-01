package org.clever.dynamic.sql;

import org.clever.dynamic.sql.builder.DynamicSqlSource;
import org.clever.dynamic.sql.builder.RawSqlSource;
import org.clever.dynamic.sql.builder.SqlSource;
import org.clever.dynamic.sql.node.TextSqlNode;
import org.clever.dynamic.sql.parsing.PropertyParser;

import java.util.Properties;

/**
 * 动态SQL解析器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/09/01 12:26 <br/>
 */
public class DynamicSqlParser {
    private DynamicSqlParser() {
    }

    public static SqlSource parserSql(String dynamicSql) {
        final Properties variables = new Properties();
        dynamicSql = PropertyParser.parse(dynamicSql, variables);
        TextSqlNode textSqlNode = new TextSqlNode(dynamicSql);
        if (textSqlNode.isDynamic()) {
            return new DynamicSqlSource(textSqlNode);
        } else {
            return new RawSqlSource(dynamicSql);
        }
    }
}
