package org.clever.dynamic.sql.scripting.xmltags;

import org.clever.dynamic.sql.mapping.DynamicSqlSource;
import org.clever.dynamic.sql.mapping.SqlSource;
import org.clever.dynamic.sql.parsing.PropertyParser;
import org.clever.dynamic.sql.parsing.XNode;
import org.clever.dynamic.sql.parsing.XPathParser;
import org.clever.dynamic.sql.scripting.LanguageDriver;
import org.clever.dynamic.sql.scripting.defaults.RawSqlSource;

import java.util.Properties;

public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(XNode script) {
        XMLScriptBuilder builder = new XMLScriptBuilder(script);
        return builder.parseScriptNode();
    }

    @Override
    public SqlSource createSqlSource(String script) {
        final Properties variables = new Properties();
        // issue #3
        if (script.startsWith("<script>")) {
            XPathParser parser = new XPathParser(script, false, variables);
            return createSqlSource(parser.evalNode("/script"));
        } else {
            // issue #127
            script = PropertyParser.parse(script, variables);
            TextSqlNode textSqlNode = new TextSqlNode(script);
            if (textSqlNode.isDynamic()) {
                return new DynamicSqlSource(textSqlNode);
            } else {
                return new RawSqlSource(script);
            }
        }
    }
}
