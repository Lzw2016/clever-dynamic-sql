package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.dynamic.sql.builder.SqlSource;
import org.clever.dynamic.sql.domain.Author;
import org.clever.dynamic.sql.domain.Section;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/01 17:14 <br/>
 */
@Slf4j
public class DynamicSqlParserTest {
    private String[] sqlArray;

    @Before
    public void init() throws Exception {
        File xml = new File("src/test/resources/sql.txt");
        String sqlText = FileUtils.readFileToString(xml, "utf-8");
        sqlArray = StringUtils.splitByWholeSeparator(sqlText, "# -------------------------------------------------------------------------------------");
    }

    /**
     * 删除多余的空白字符
     */
    private static String deleteWhitespace(String sql) {
        if (sql == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(sql.length());
        boolean preIsWhitespace = false;
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            boolean isWhitespace = Character.isWhitespace(ch);
            if (preIsWhitespace) {
                // 之前是空白字符
                if (isWhitespace) {
                    // 当前是空白字符
                    continue;
                } else {
                    // 当前非空白字符
                    sb.append(ch);
                }
            } else {
                // 之前非空白字符
                if (isWhitespace) {
                    // 当前是空白字符
                    sb.append(' ');
                } else {
                    // 当前非空白字符
                    sb.append(ch);
                }
            }
            preIsWhitespace = isWhitespace;
        }
        return sb.toString();
    }

    // 简单脚本
    @Test
    public void t01() {
        String sql = sqlArray[0];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2);
        params.put("name", "%queryAll%");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 复杂脚本
    @Test
    public void t02() {
        String sql = sqlArray[1];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
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
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 参数是对象
    @Test
    public void t03() {
        String sql = sqlArray[2];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        BoundSql boundSql = sqlSource.getBoundSql(author);
        boundSql.getParameterValueList();
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 混合参数
    @Test
    public void t04() {
        String sql = sqlArray[3];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        Map<String, Object> params = new HashMap<>();
        params.put("author", author);
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 性能测试
    @Test
    public void t05() {
        String sql = sqlArray[4];
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
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        final int count = 100000;
        final long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            BoundSql boundSql = sqlSource.getBoundSql(params);
            boundSql.getParameterValueList();
        }
        final long end = System.currentTimeMillis();
        log.info("--> 耗时 {}s, 速度： {}次/ms", (end - start) / 1000.0, count * 1.0 / (end - start));
    }

    @Test
    public void t06() {
        String sql = sqlArray[5];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap()); // TODO bind 参数 pattern 没有值
    }

    @Test
    public void t07() {
        String sql = sqlArray[6];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }
}
