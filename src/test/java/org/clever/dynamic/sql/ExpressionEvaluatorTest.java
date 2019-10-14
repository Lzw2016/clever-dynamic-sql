package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.domain.blog.Author;
import org.clever.dynamic.sql.domain.blog.Section;
import org.clever.dynamic.sql.scripting.xmltags.ExpressionEvaluator;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/14 10:48 <br/>
 */
@Slf4j
public class ExpressionEvaluatorTest {

    private ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Test
    public void t1() {
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        boolean value = evaluator.evaluateBoolean("username == 'cbegin'", author);
        log.info("username == 'cbegin' -> {}", value);

        value = evaluator.evaluateBoolean("username == 'lzw'", author);
        log.info("username == 'lzw' -> {}", value);

        value = evaluator.evaluateBoolean("username", author);
        log.info("username -> {}", value);

        value = evaluator.evaluateBoolean("id", author);
        log.info("id -> {}", value);
    }
}
