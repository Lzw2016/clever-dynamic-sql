package org.clever.dynamic.sql.scripting;

import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.parsing.XNode;

public interface LanguageDriver {
    /**
     * Creates an {@link SqlSource} that will hold the statement read from a mapper xml file.
     * It is called during startup, when the mapped statement is read from a class or an xml file.
     *
     * @param script XNode parsed from a XML file
     */
    SqlSource createSqlSource(XNode script);

    /**
     * Creates an {@link SqlSource} that will hold the statement read from an annotation.
     * It is called during startup, when the mapped statement is read from a class or an xml file.
     *
     * @param script The content of the annotation
     */
    SqlSource createSqlSource(String script);

}
