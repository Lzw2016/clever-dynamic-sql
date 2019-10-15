package org.clever.dynamic.sql.reflection;

public interface ReflectorFactory {

    boolean isClassCacheEnabled();

    void setClassCacheEnabled(boolean classCacheEnabled);

    Reflector findForClass(Class<?> type);
}