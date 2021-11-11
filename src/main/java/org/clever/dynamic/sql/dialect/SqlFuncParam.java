package org.clever.dynamic.sql.dialect;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 17:37 <br/>
 */
@Data
public class SqlFuncParam implements Serializable {
    /**
     * 参数字面值
     */
    private final String literal;
    /**
     * 参数名
     */
    private final String name;
    /**
     * 参数值
     */
    private final Object value;
    /**
     * 是否是变量
     */
    private final boolean variable;

    public SqlFuncParam(String literal, String name, Object value, boolean variable) {
        this.literal = literal;
        this.name = name;
        this.value = value;
        this.variable = variable;
    }
}
