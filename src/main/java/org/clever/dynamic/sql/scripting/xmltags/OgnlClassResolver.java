package org.clever.dynamic.sql.scripting.xmltags;

import ognl.DefaultClassResolver;
import org.clever.dynamic.sql.io.Resources;

public class OgnlClassResolver extends DefaultClassResolver {

    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        return Resources.classForName(className);
    }
}
