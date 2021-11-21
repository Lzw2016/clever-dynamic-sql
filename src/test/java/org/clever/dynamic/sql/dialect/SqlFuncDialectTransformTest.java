package org.clever.dynamic.sql.dialect;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.clever.dynamic.sql.dialect.func.ToDateFuncTransform;
import org.clever.dynamic.sql.dialect.utils.SqlFuncTransformUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 17:13 <br/>
 */
@Slf4j
public class SqlFuncDialectTransformTest {

    public SqlFuncDialectTransformTest() {
        SqlFuncTransformUtils.register(new ToDateFuncTransform());
    }

    public SqlFuncDialectTransform parser(String code, DbType dbType, Map<String, Object> ognlRoot) {
        final long startTime = System.currentTimeMillis();
        CharStream charStream = CharStreams.fromString(code);
        SqlFuncLexer lexer = new SqlFuncLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SqlFuncParser parser = new SqlFuncParser(tokenStream);
        ParseTree tree = parser.sqlFunc();
        ParseTreeWalker walker = new ParseTreeWalker();
        SqlFuncDialectTransform transform = new SqlFuncDialectTransform(dbType, ognlRoot, lexer, tokenStream, parser);
        walker.walk(transform, tree);
        final long endTime = System.currentTimeMillis();
        log.info("耗时：{}ms", endTime - startTime);
        return transform;
    }


    @Test
    public void t01() {
        // sql_func_1(jObject.fieldA.java_func(), jObject.fieldB, "YYYY-DD-MM", jVar2, jVar3)
        final String code = "sql_func_1(jObject.fieldA.java_func(), jObject.fieldB, \"YYYY-DD-MM\", jVar2, jVar3)";
        SqlFuncDialectTransform transform = parser(code, DbType.MYSQL, new HashMap<>());
        log.info("--> {} | {}", transform.toSql(), transform.getSqlVariable());
    }

    @Test
    public void t02() {
        // sql_func_1(jObject.fieldA.java_func(jObject.fieldC, "abc"), jObject.fieldB, "YYYY-DD-MM", jVar2, jVar3)
        final String code = "sql_func_1(jObject.fieldA.java_func(jObject.fieldC, \"abc\"), jObject.fieldB, \"YYYY-DD-MM\", jVar2, jVar3)";
        SqlFuncDialectTransform transform = parser(code, DbType.MYSQL, new HashMap<>());
        log.info("--> {} | {}", transform.toSql(), transform.getSqlVariable());
    }

    @Test
    public void t03() {
        // sql_func_1(jObject.java_func_1(jVar2, "ABC"), jVar3, sql_func_2(jVar3, jObject.java_func_2(jVar2, "ABC")))
        final String code = "sql_func_1(jObject.java_func_1(jVar2, \"ABC\"), jVar3, sql_func_2(jVar3, jObject.java_func_2(jVar2, \"ABC\")))";
        SqlFuncDialectTransform transform = parser(code, DbType.MYSQL, new HashMap<>());
        log.info("--> {} | {}", transform.toSql(), transform.getSqlVariable());
    }

    @Test
    public void t04() {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("today", new Date());

        // to_date(today)
        final String code = "to_date(today)";

        SqlFuncDialectTransform transform = parser(code, DbType.MYSQL, bindings);
        log.info("MYSQL        --> {} | {}", transform.toSql(), transform.getSqlVariable());

        transform = parser(code, DbType.ORACLE, bindings);
        log.info("ORACLE       --> {} | {}", transform.toSql(), transform.getSqlVariable());

        transform = parser(code, DbType.SQL_SERVER, bindings);
        log.info("SQL_SERVER   --> {} | {}", transform.toSql(), transform.getSqlVariable());
    }

    @Test
    public void t05() {
        final LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("today1", new Date());
        bindings.put("today2", new Date());
        String sql = "select * from entity_a where a='@{to_date(today1)}' and b='@{to_date(today2)}' and c=#{abc}";
        Pattern pattern = Pattern.compile("'@\\{.+?}'");
        Matcher matcher = pattern.matcher(sql);
        StringBuilder newSql = new StringBuilder();
        int start = 0;
        while (matcher.find()) {
            newSql.append(sql, start, matcher.start());
            String code = matcher.group();
            code = code.substring(3, code.length() - 2);
            start = matcher.end();
            SqlFuncDialectTransform transform = parser(code, DbType.MYSQL, bindings);
            newSql.append(transform.toSql());
            params.putAll(transform.getSqlVariable());
        }
        newSql.append(sql, start, sql.length());
        log.info("newSql   --> {} | {}", newSql, params);
    }
}
