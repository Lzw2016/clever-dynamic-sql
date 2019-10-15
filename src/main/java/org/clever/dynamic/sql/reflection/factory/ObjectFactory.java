package org.clever.dynamic.sql.reflection.factory;

import java.util.List;
import java.util.Properties;

public interface ObjectFactory {

    /**
     * Sets configuration properties.
     *
     * @param properties configuration properties
     */
    default void setProperties(Properties properties) {
        // NOP
    }

    /**
     * Creates a new object with default constructor.
     *
     * @param type Object type
     */
    <T> T create(Class<T> type);

    /**
     * Creates a new object with the specified constructor and params.
     *
     * @param type                Object type
     * @param constructorArgTypes Constructor argument types
     * @param constructorArgs     Constructor argument values
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * Returns true if this object can have a set of other objects.
     * It's main purpose is to support non-java.util.Collection objects like Scala collections.
     *
     * @param type Object type
     * @return whether it is a collection or not
     */
    <T> boolean isCollection(Class<T> type);

}
