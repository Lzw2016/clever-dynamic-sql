package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.clever.dynamic.sql.builder.SqlSource;
import org.clever.dynamic.sql.parsing.XNode;
import org.clever.dynamic.sql.parsing.XPathParser;
import org.clever.dynamic.sql.utils.TestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/10 10:23 <br/>
 */
@Slf4j
public class MapperXmlParserTest {

    // 读取 mybatis mapper.xml
    @Test
    public void t11() throws Exception {
        File xml = new File("src/test/resources/sql.xml");
        // String sqlText = FileUtils.readFileToString(xml, "utf-8");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        factory.setExpandEntityReferences(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        Document document = builder.parse(xml);
        Node node = (Node) xpath.evaluate("/mapper", document, XPathConstants.NODE);
        NodeList nodes = (NodeList) xpath.evaluate("sql|select|insert|update|delete", node, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(n), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            log.info("\n\n{}\n\n", output);
        }
    }

    // 读取 mybatis mapper.xml
    @Test
    public void t12() throws Exception {
        final Properties variables = new Properties();
        File xml = new File("src/test/resources/sql.xml");
        XPathParser xPathParser = new XPathParser(FileUtils.openInputStream(xml), false, variables);
        SqlSource sqlSource = DynamicSqlParser.parserSql(xPathParser.evalNode("/mapper/select"));
        Map<String, Object> params = new HashMap<>();
        params.put("f1", "123");
        params.put("f2", "   ");
        params.put("f3", "bbb");
        params.put("f4", new int[]{1, 2, 3});
        params.put("f5", new ArrayList<String>() {{
            add("f5-aaa");
            add("f5-bbb");
            add("f5-ccc");
        }});
        params.put("f6", new BigDecimal("999"));
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", TestUtils.deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", TestUtils.deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());

        List<XNode> nodes = xPathParser.evalNode("/mapper").evalNodes("sql|select|insert|update|delete");
        log.info("nodes --> {}", nodes.size());
    }
}
