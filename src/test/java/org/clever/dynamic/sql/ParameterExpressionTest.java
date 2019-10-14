package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.builder.ParameterExpression;
import org.junit.Test;

import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/14 16:49 <br/>
 */
@Slf4j
public class ParameterExpressionTest {

    @Test
    public void t1() {
        Map<String, String> result = new ParameterExpression("(id.toString()):VARCHAR");
        result.forEach((s, s2) -> log.info("{} -> {}", s, s2));
        log.info("--------------------------------------------------------------------------");
        result = new ParameterExpression("id");
        result.forEach((s, s2) -> log.info("{} -> {}", s, s2));
        log.info("--------------------------------------------------------------------------");
        result = new ParameterExpression("id:VARCHAR");
        result.forEach((s, s2) -> log.info("{} -> {}", s, s2));
    }
}
