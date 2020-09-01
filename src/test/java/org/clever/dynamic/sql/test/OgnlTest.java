package org.clever.dynamic.sql.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import org.clever.dynamic.sql.domain.Author;
import org.junit.Test;

import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/01 14:14 <br/>
 */
@Slf4j
public class OgnlTest {

    @SneakyThrows
    @Test
    public void t01() {
        Author author = new Author();
        author.setId(11);
        author.setUsername("lzw");
//        Map context = Ognl.createDefaultContext(author,
//                new DefaultMemberAccess(true),
//                new DefaultClassResolver(),
//                new DefaultTypeConverter());
//        context.put("author", author);
        PropertyAccessor propertyAccessor = new MyPropertyAccessor();
        OgnlRuntime.setPropertyAccessor(Author.class, propertyAccessor);
        Object obj = Ognl.getValue("#author.username", author);
        log.info("username -> {}", obj);
    }
}

class MyPropertyAccessor implements PropertyAccessor {
    @SneakyThrows
    @Override
    public Object getProperty(Map context, Object target, Object name) {
        String nameTmo = name.toString();
        OgnlContext ognlContext = (OgnlContext) context;
        Object result = OgnlRuntime.getMethodValue(ognlContext, target, nameTmo, true);
        return result + "123";
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) {
        System.out.println("setProperty");
    }

    @Override
    public String getSourceAccessor(OgnlContext ognlContext, Object o, Object o1) {
        System.out.println("getSourceAccessor");
        return null;
    }

    @Override
    public String getSourceSetter(OgnlContext ognlContext, Object o, Object o1) {
        System.out.println("getSourceSetter");
        return null;
    }
}