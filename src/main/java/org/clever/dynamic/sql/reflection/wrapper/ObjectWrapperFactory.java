package org.clever.dynamic.sql.reflection.wrapper;

import org.clever.dynamic.sql.reflection.MetaObject;

public interface ObjectWrapperFactory {

    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
