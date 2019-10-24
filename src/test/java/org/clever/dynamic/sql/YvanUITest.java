package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.mapping.BoundSql;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.scripting.xmltags.XMLLanguageDriver;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/24 12:02 <br/>
 */
@Slf4j
public class YvanUITest {

    @Test
    public void t1() {
        String sql = "<script>" +
                "   <bind name='pattern' value='title4' />" + // bind
                "   select" +
                "       * " +
                "   from sql_script " +
                "   where id=#{id} " +          // 普通参数
                "       and name=${name} " +    // 字符串替换
                "       and id in ( <foreach collection='list' item='item' separator=','>#{item}</foreach> ) " + // foreach 循环
                "       <if test='title != null'>and title=#{title}</if>" + // if
                "       <choose>" +             // choose
                "           <when test='title2'>" +
                "               and title2=#{title2}" +
                "           </when>" +
                "           <when test='title3'>" +
                "               and title5=#{title5}" +
                "           </when>" +
                "           <otherwise>" +
                "               and featured=#{featured}" +
                "           </otherwise>" +
                "       </choose>" +
                "       and title4 like #{pattern}" +
                "   order by ${orderBy}" +
                "</script>";

        // featured name orderBy title title2 title3 id title4 list title5
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
        // [featured, name, orderBy, title != null, title2, title3, id, title4, list]
        log.info("--> {}", boundSql.getParameterExpressionSet());
    }

    @Test
    public void t2() {
        // java.lang.NullPointerException: target is null for method getName
        String sql = "<script>" +
                "   <bind name='pattern' value='title4' />" + // bind
                "   select" +
                "       * " +
                "   from sql_script " +
                "   where id=#{id} " +          // 普通参数
                "       and name=${name} " +    // 字符串替换
                "       and id in ( <foreach collection='list' item='item' separator=','>#{item}</foreach> ) " + // foreach 循环
                "       <if test='title != null'>and title=#{title}</if>" + // if
                "       <choose>" +             // choose
                "           <when test='title2.name'>" +
                "               and title2=#{title2}" +
                "           </when>" +
                "           <when test='title3'>" +
                "               and title5=#{title5}" +
                "           </when>" +
                "           <otherwise>" +
                "               and featured=#{featured}" +
                "           </otherwise>" +
                "       </choose>" +
                "       and title4 like #{pattern}" +
                "   order by ${orderBy}" +
                "</script>";

        // featured name orderBy title title2 title3 id title4 list title5
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
        log.info("--> {}", boundSql.getSql());
        // BoundSql 支持命名参数
        log.info("--> {}", boundSql.getNamedParameterSql());
        // [featured, name, orderBy, title != null, title2, title3, id, title4, list]
        log.info("--> {}", boundSql.getParameterExpressionSet());
    }
}
