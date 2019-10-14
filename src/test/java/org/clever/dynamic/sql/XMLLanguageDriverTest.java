package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.scripting.xmltags.XMLLanguageDriver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/14 13:00 <br/>
 */
@Slf4j
public class XMLLanguageDriverTest {
    @Test
    public void t1() {
        String sql = "select * from sql_script where id=#{id} and name like '${name}'";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql, Map.class);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2);
        params.put("name", "%queryAll%");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", boundSql.getSql());
    }

    @Test
    public void t2() {
        String sql = "<script>" +
                "select * from sql_script where id in ( " +
                "    <foreach collection='list' item='item' separator=','>#{item}</foreach> " +
                ") " +
                "and name in (" +
                "    <foreach collection='names' item='item' separator=','>#{item}</foreach>" +
                ")" +
                " order by ${orderBy}" +
                "</script>";

//        sql = "<script> select * from sql_script where id=#{id} and name like '${name}' </script>";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql, Map.class);
        Map<String, Object> params = new HashMap<>();
        List<Integer> list = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
            add(6);
        }};
        params.put("list", list);
        List<String> names = new ArrayList<String>() {{
            add("name1");
            add("name2");
            add("name3");
            add("name4");
            add("name5");
            add("name6");
        }};
        params.put("names", names);
        params.put("orderBy", "a.aaa DESC, a.bbb ASC");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", boundSql.getSql());
    }
}