package org.clever.dynamic.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.dynamic.sql.builder.SqlSource;
import org.clever.dynamic.sql.domain.Author;
import org.clever.dynamic.sql.domain.Section;
import org.clever.dynamic.sql.ognl.OgnlCache;
import org.clever.dynamic.sql.parsing.XNode;
import org.clever.dynamic.sql.parsing.XPathParser;
import org.junit.Before;
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
 * 创建时间：2020/09/01 17:14 <br/>
 */
@Slf4j
public class DynamicSqlParserTest {
    private String[] sqlArray;

    @Before
    public void init() throws Exception {
        File xml = new File("src/test/resources/sql.txt");
        String sqlText = FileUtils.readFileToString(xml, "utf-8");
        sqlArray = StringUtils.splitByWholeSeparator(sqlText, "# -------------------------------------------------------------------------------------");
    }

    /**
     * 删除多余的空白字符
     */
    private static String deleteWhitespace(String sql) {
        if (sql == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(sql.length());
        boolean preIsWhitespace = false;
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            boolean isWhitespace = Character.isWhitespace(ch);
            if (preIsWhitespace) {
                // 之前是空白字符
                if (isWhitespace) {
                    // 当前是空白字符
                    continue;
                } else {
                    // 当前非空白字符
                    sb.append(ch);
                }
            } else {
                // 之前非空白字符
                if (isWhitespace) {
                    // 当前是空白字符
                    sb.append(' ');
                } else {
                    // 当前非空白字符
                    sb.append(ch);
                }
            }
            preIsWhitespace = isWhitespace;
        }
        return sb.toString();
    }

    // 简单脚本
    @Test
    public void t01() {
        String sql = sqlArray[0];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2);
        params.put("name", "%queryAll%");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 复杂脚本
    @Test
    public void t02() {
        String sql = sqlArray[1];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("id", "value111111111111111111");
        params.put("name", "lzw");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        params.put("list", list);
        List<String> names = new ArrayList<>();
        names.add("name1");
        names.add("name2");
        names.add("name3");
        names.add("name4");
        names.add("name5");
        names.add("name6");
        params.put("names", names);
        params.put("orderBy", "a.aaa DESC, a.bbb ASC");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
        log.info("#---------------------------------------------------------------------------------------------------------");
        boundSql = sqlSource.getBoundSql(params);
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
        log.info("#---------------------------------------------------------------------------------------------------------");
        params.clear();
        boundSql = sqlSource.getBoundSql(params);
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 参数是对象
    @Test
    public void t03() {
        String sql = sqlArray[2];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        BoundSql boundSql = sqlSource.getBoundSql(author);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 混合参数
    @Test
    public void t04() {
        String sql = sqlArray[3];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
        Map<String, Object> params = new HashMap<>();
        params.put("author", author);
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // 性能测试
    @Test
    public void t05() {
        String sql = sqlArray[4];
        Map<String, Object> params = new HashMap<>();
        params.put("id", "value111111111111111111");
        params.put("name", "lzw");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        params.put("list", list);
        List<String> names = new ArrayList<>();
        names.add("name1");
        names.add("name2");
        names.add("name3");
        names.add("name4");
        names.add("name5");
        names.add("name6");
        params.put("names", names);
        params.put("orderBy", "a.aaa DESC, a.bbb ASC");
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        final int count = 100000;
        final long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            BoundSql boundSql = sqlSource.getBoundSql(params);
            boundSql.getParameterValueList();
        }
        final long end = System.currentTimeMillis();
        log.info("--> 耗时 {}s, 速度： {}次/ms", (end - start) / 1000.0, count * 1.0 / (end - start));
    }

    @Test
    public void t06() {
        String sql = sqlArray[5];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    @Test
    public void t07() {
        String sql = sqlArray[6];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // mybatis 兼容性全面测试
    @Test
    public void t08() {
        String sql = sqlArray[7];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("f1", "f1-val");
        params.put("f2", "f2-val");
        params.put("f3", "f3-val");
        params.put("f4List", Arrays.asList("f4-111", "f4-222", "f4-333"));
        params.put("f5", "f5-val");
        params.put("f6", "f6-val");
        params.put("f7", "f7-val");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // mybatis 兼容性全面测试
    @Test
    public void t09() {
        String sql = sqlArray[8];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
        Map<String, Object> params = new HashMap<>();
        params.put("f1", "f1-val");
        params.put("f2", "f2-val");
        params.put("f3", "f3-val");
        params.put("f4", "f4-val");
        params.put("id", "id-val");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
    }

    // mybatis 扩展函数
    @Test
    public void t10() {
        String sql = sqlArray[9];
        SqlSource sqlSource = DynamicSqlParser.parserSql(sql);
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
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());
        OgnlCache.Default_Context.put("demo", "自定义");
    }

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
        log.info("--> {}", deleteWhitespace(boundSql.getSql()));
        log.info("--> {}", boundSql.getParameterValueList());
        log.info("--> {} ", deleteWhitespace(boundSql.getNamedParameterSql()));
        log.info("--> {}", boundSql.getParameterMap());

        List<XNode> nodes = xPathParser.evalNode("/mapper").evalNodes("sql|select|insert|update|delete");
        log.info("nodes --> {}", nodes.size());
    }
}
