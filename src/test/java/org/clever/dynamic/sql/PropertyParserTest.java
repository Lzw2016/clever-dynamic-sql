package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.parsing.PropertyParser;
import org.junit.Test;

import java.util.Properties;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/12 11:14 <br/>
 */
@Slf4j
public class PropertyParserTest {

    @Test
    public void t1() {
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "true");
        props.setProperty("tableName", "members");
        props.setProperty("orderColumn", "member_id");
        props.setProperty("a:b", "c");

        String str = PropertyParser.parse("${key:default123}", props);
        log.info("--> {}", str);

        str = PropertyParser.parse("${tableName:111}", props);
        log.info("--> {}", str);
    }
}
