package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.builder.SqlSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/01 17:14 <br/>
 */
@Slf4j
public class DynamicSqlParserTest {



    // 简单脚本
    @Test
    public void t01() {
        String sql = "";
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2);
        params.put("name", "%queryAll%");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {} | {}", boundSql.getSql(), boundSql.getParameterValueList());
        log.info("--> {} | {}", boundSql.getNamedParameterSql(), boundSql.getParameterMap());
    }

//    @Test
//    public void t02() {
//        String sql = ;
//        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", 2);
//        params.put("name", "%queryAll%");
//        BoundSql boundSql = sqlSource.getBoundSql(params);
//        boundSql.getParameterValueList();
//        log.info("--> {} | {}", boundSql.getSql(), boundSql.getParameterValueList());
//        log.info("--> {} | {}", boundSql.getNamedParameterSql(), boundSql.getParameterMap());
//    }
}
