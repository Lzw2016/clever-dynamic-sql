# clever-dynamic-sql
动态SQL脚本生成(完全兼容mybatis)

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
--> select * from sql_script where id=? and name=? and id in ( ? , ? , ? , ? , ? , ? ) and name in ( ? , ? , ? , ? , ? , ? ) order by a.aaa DESC, a.bbb ASC
--> select * from sql_script where id=:id and name=:name and id in ( :__frch_item_0 , :__frch_item_1 , :__frch_item_2 , :__frch_item_3 , :__frch_item_4 , :__frch_item_5 )  and name in ( :__frch_item_6 , :__frch_item_7 , :__frch_item_8 , :__frch_item_9 , :__frch_item_10 , :__frch_item_11 ) order by a.aaa DESC, a.bbb ASC
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
    SqlSource sqlSource = xmlLanguageDriver.createSqlSource(sql);
    for (int i = 0; i < count; i++) {
        BoundSql boundSql = sqlSource.getBoundSql(params);
        boundSql.getParameterValueList();
    }
    final long end = System.currentTimeMillis();
    log.info("--> 耗时 {}s, 速度： {}次/ms", (end - start) / 1000.0, count / (end - start));
}
```

输出:

```
--> 耗时 2.9s, 速度： 34.48275862068966次/ms
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

## MyBatis 扩展

#### 扩展函数

- `#obj.notEmpty(param)` 判断对象不为null，字符串不为空字符串，数组集合不为空。**最常用，符合所有的空判断**
- `#obj.hasValue(param)` 同`#obj.notEmpty(...)`，只是一个`notEmpty`别名
- `#obj.exists(param)` 同`#obj.notEmpty(...)`，只是一个`notEmpty`别名
- `#obj.isIn(param, val_1, val_2', ...)` 判断`param`参数值在给定的范围(`val_1, val_2', ...`)内
- `#obj.isInIgnoreCase(param, val_1, val_2', val_3, ...)` 与`#obj.isIn(...)`类似，仅仅只是针对字符串值，并且忽略字符串大小写
- `#str.isNotBlank(param)` 字符串不为空字符串
- `#str.isBlank(param)` 字符串是空字符串
- `#str.hasText(param)` 字符串不为空字符串，`#str.isNotBlank(...)`别名
- `#str.hasLength(param)`字符串长度大于0
- `#str.contains(param, searchStr)` 字符串是否包含一个子字符串
- `#str.containsIgnoreCase(param, searchStr)` 字符串是否包含一个子字符串，不区分大小写
- `#str.trim(param)` 删除字符串头尾空格

#### 使用示例

```xml
<select>
    select * from sql_script
    <where>
        <if test="#str.isNotBlank(f1)">
            and f1=#{f1}
        </if>
        <if test="#str.isNotBlank(f2)">
            and f2=#{f2}
        </if>
        <if test="#obj.isIn(f3, 'aaa', 'bbb', 'ccc', 1, false)">
            and f3=#{f3}
        </if>
        <if test="#obj.notEmpty(f1)">
            and f1=#{f1}
        </if>
        <if test="#obj.notEmpty(f4)">
            and f4=#{f4}
        </if>
        <if test="#obj.notEmpty(f5)">
            and f5=#{f5}
        </if>
        <if test="#obj.notEmpty(f6)">
            and f6=#{f6}
        </if>
    </where>
</select>
```
