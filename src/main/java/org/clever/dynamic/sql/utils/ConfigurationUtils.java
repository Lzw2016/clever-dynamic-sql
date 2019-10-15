package org.clever.dynamic.sql.utils;

import org.clever.dynamic.sql.reflection.DefaultReflectorFactory;
import org.clever.dynamic.sql.reflection.MetaObject;
import org.clever.dynamic.sql.reflection.ReflectorFactory;
import org.clever.dynamic.sql.reflection.factory.DefaultObjectFactory;
import org.clever.dynamic.sql.reflection.factory.ObjectFactory;
import org.clever.dynamic.sql.reflection.wrapper.DefaultObjectWrapperFactory;
import org.clever.dynamic.sql.reflection.wrapper.ObjectWrapperFactory;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/15 09:32 <br/>
 */
public class ConfigurationUtils {
    private static final ObjectFactory objectFactory = new DefaultObjectFactory();
    private static final ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    public static MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory);
    }
}
