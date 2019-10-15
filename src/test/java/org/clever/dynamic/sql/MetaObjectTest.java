package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.domain.blog.Author;
import org.clever.dynamic.sql.domain.blog.Section;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.reflection.SystemMetaObject;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/15 09:56 <br/>
 */
@Slf4j
public class MetaObjectTest {

    @Test
    public void t1() {
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        MetaObject meta = SystemMetaObject.forObject(author);
        log.info(" --> {}", meta.getValue("username"));
    }
}
