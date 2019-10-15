package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.domain.blog.Author;
import org.clever.dynamic.sql.domain.blog.Section;
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

    // 简单脚本
    @Test
    public void t1() {
        String sql = "select * from sql_script where id=#{id} and name like '${name}'";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2);
        params.put("name", "%queryAll%");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
    }

    // 参数是 Map
    @Test
    public void t2() {
        String sql = "<script>" +
                "   select * from sql_script where id=#{id} and name=#{name} and id in ( " +
                "       <foreach collection='list' item='item' separator=','>#{item}</foreach> " +
                "   ) " +
                "   and name in (" +
                "       <foreach collection='names' item='item' separator=','>#{item}</foreach>" +
                "   )" +
                "   order by ${orderBy}" +
                "</script>";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", "value111111111111111111");
        params.put("name", "lzw");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        params.put("list", list);
        List<String> names = new ArrayList<>();
        names.add("name1");
        names.add("name2");
        names.add("name3");
        names.add("name4");
        names.add("name5");
        names.add("name6");
        params.put("names", names);
        params.put("orderBy", "a.aaa DESC, a.bbb ASC");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
    }

    // 参数是 对象
    @Test
    public void t3() {
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        String sql = "<script>select * from author where username=#{username}</script>";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        BoundSql boundSql = sqlSource.getBoundSql(author);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
    }

    // 混合参数
    @Test
    public void t5() {
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        String sql = "<script>select * from author where username=#{author.username}</script>";
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("author", author);
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
    }

    // 性能测试
    @Test
    public void t6() {
        String sql = "<script>" +
                "   select * from sql_script where id=#{id} and name=#{name} and id in ( " +
                "       <foreach collection='list' item='item' separator=','>#{item}</foreach> " +
                "   ) " +
                "   and name in (" +
                "       <foreach collection='names' item='item' separator=','>#{item}</foreach>" +
                "   )" +
                "   order by ${orderBy}" +
                "</script>";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "value111111111111111111");
        params.put("name", "lzw");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        params.put("list", list);
        List<String> names = new ArrayList<>();
        names.add("name1");
        names.add("name2");
        names.add("name3");
        names.add("name4");
        names.add("name5");
        names.add("name6");
        params.put("names", names);
        params.put("orderBy", "a.aaa DESC, a.bbb ASC");
        final int count = 100000;
        final long start = System.currentTimeMillis();
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        for (int i = 0; i < count; i++) {
            SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
            BoundSql boundSql = sqlSource.getBoundSql(params);
            boundSql.getParameterValueList();
        }
        final long end = System.currentTimeMillis();
        log.info("--> 耗时 {}s, 速度： {}次/ms", (end - start) / 1000.0, count / (end - start));
    }
}
