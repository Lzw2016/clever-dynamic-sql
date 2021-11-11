package org.clever.dynamic.sql.dialect;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncLexer;
import org.clever.dynamic.sql.dialect.antlr.SqlFuncParser;
import org.junit.jupiter.api.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 17:13 <br/>
 */
@Slf4j
public class SqlFuncDialectTransformTest {

    public void parser(String code) {
        final long startTime = System.currentTimeMillis();
        CharStream charStream = CharStreams.fromString(code);
        SqlFuncLexer lexer = new SqlFuncLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SqlFuncParser parser = new SqlFuncParser(tokenStream);
        ParseTree tree = parser.sqlFunc();
        ParseTreeWalker walker = new ParseTreeWalker();
        SqlFuncDialectTransform transform = new SqlFuncDialectTransform(lexer, tokenStream, parser);
        walker.walk(transform, tree);
        final long endTime = System.currentTimeMillis();
        log.info("耗时：{}ms", endTime - startTime);
    }


    @Test
    public void t01() {
        // sql_func_1(jObject.fieldA.java_func(), jObject.fieldB, "YYYY-DD-MM", jVar2, jVar3)
        final String code = "sql_func_1(jObject.fieldA.java_func(), jObject.fieldB, \"YYYY-DD-MM\", jVar2, jVar3)";
        parser(code);
    }

    @Test
    public void t02() {
        // sql_func_1(jObject.fieldA.java_func(jObject.fieldC, "abc"), jObject.fieldB, "YYYY-DD-MM", jVar2, jVar3)
        final String code = "sql_func_1(jObject.fieldA.java_func(jObject.fieldC, \"abc\"), jObject.fieldB, \"YYYY-DD-MM\", jVar2, jVar3)";
        parser(code);
    }

    @Test
    public void t03() {
        // sql_func_1(jObject.java_func_1(jVar2, "ABC"), jVar3, sql_func_2(jVar3, jObject.java_func_2(jVar2, "ABC")))
        final String code = "sql_func_1(jObject.java_func_1(jVar2, \"ABC\"), jVar3, sql_func_2(jVar3, jObject.java_func_2(jVar2, \"ABC\")))";
        parser(code);
    }
}
