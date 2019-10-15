# clever-dynamic-sql
动态SQL脚本生成(兼容mybatis)

## 使用Demo

#### 简单脚本
```java
@Test
public void t1() {
    String sql = "select * from sql_script where id=#{id} and name like '${name}'";
    XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
    Map<String, Object> params = new HashMap<>();
    params.put("id", 2);
    params.put("name", "%queryAll%");
    BoundSql boundSql = sqlSource.getBoundSql(params);
    boundSql.getParameterValueList();
    log.info("--> {}", boundSql.getSql());
    // BoundSql 支持命名参数
    log.info("--> {}", boundSql.getNamedParameterSql());
}
```

输出:

```
--> select * from sql_script where id=? and name like '%queryAll%'
--> select * from sql_script where id=:id and name like '%queryAll%'
```

#### 参数是Map

```java
@Test
public void t2() {
    String sql = 
            "<script>" +
            "   select * from sql_script where id=#{id} and name=#{name} and id in ( " +
            "       <foreach collection='list' item='item' separator=','>#{item}</foreach> " +
            "   ) " +
            "   and name in (" +
            "       <foreach collection='names' item='item' separator=','>#{item}</foreach>" +
            "   )" +
            "   order by ${orderBy}" +
            "</script>";
    XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
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
    boundSql.getParameterValueList();
    log.info("--> {}", boundSql.getSql());
    // BoundSql 支持命名参数
    log.info("--> {}", boundSql.getNamedParameterSql());
}
```

输出:

```
--> select * from sql_script where id=? and name=? and id in (          ? , ? , ? , ? , ? , ?     )    and name in (         ? , ? , ? , ? , ? , ?    )   order by a.aaa DESC, a.bbb ASC
--> select * from sql_script where id=:id and name=:name and id in (          :__frch_item_0 , :__frch_item_1 , :__frch_item_2 , :__frch_item_3 , :__frch_item_4 , :__frch_item_5     )    and name in (         :__frch_item_6 , :__frch_item_7 , :__frch_item_8 , :__frch_item_9 , :__frch_item_10 , :__frch_item_11    )   order by a.aaa DESC, a.bbb ASC
```

#### 参数是对象

```java
@Test
public void t3() {
    Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
    String sql = "<script>select * from author where username=#{username}</script>";
    XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
    BoundSql boundSql = sqlSource.getBoundSql(author);
    boundSql.getParameterValueList();
    log.info("--> {}", boundSql.getSql());
    // BoundSql 支持命名参数
    log.info("--> {}", boundSql.getNamedParameterSql());
}
```

输出:

```
--> select * from author where username=?
--> select * from author where username=:username
```

#### 混合参数

```java
@Test
public void t5() {
    Author author = new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS);
    String sql = "<script>select * from author where username=#{author.username}</script>";
    XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
    Map<String, Object> params = new HashMap<>();
    params.put("author", author);
    BoundSql boundSql = sqlSource.getBoundSql(params);
    boundSql.getParameterValueList();
    log.info("--> {}", boundSql.getSql());
    // BoundSql 支持命名参数
    log.info("--> {}", boundSql.getNamedParameterSql());
}
```

输出:

```
--> select * from author where username=?
--> select * from author where username=:author.username
```


#### 简单性能测试

```java
@Test
public void t6() {
    String sql = "<script>" +
            "   select * from sql_script where id=#{id} and name=#{name} and id in ( " +
            "       <foreach collection='list' item='item' separator=','>#{item}</foreach> " +
            "   ) " +
            "   and name in (" +
            "       <foreach collection='names' item='item' separator=','>#{item}</foreach>" +
            "   )" +
            "   order by ${orderBy}" +
            "</script>";
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
    final int count = 100000;
    final long start = System.currentTimeMillis();
    XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    for (int i = 0; i < count; i++) {
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
    }
    final long end = System.currentTimeMillis();
    log.info("--> 耗时 {}s, 速度： {}次/ms", (end - start) / 1000.0, count / (end - start));
}
```

输出:

```
--> 耗时 20.018s, 速度： 4次/ms
```

## BoundSql 结构

```
public class BoundSql {
    /**
     * 生成的SQL语句
     */
    private final String sql;
    /**
     * 参数名称形式的sql
     */
    private final String namedParameterSql;
    /**
     * Sql参数名称列表(有顺序)
     */
    private final List<String> parameterList;
    /**
     * Sql参数值列表(有顺序)
     */
    private List<Object> parameterValueList;
    /**
     * Sql参数Map集合
     */
    private Map<String, Object> parameterMap;
}
```






