package org.clever.dynamic.sql.dialect;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/21 13:50 <br/>
 */
@Data
public class SqlFuncNode {
    /**
     * SQL函数名称
     */
    private final String funcName;
    /**
     * SQL函数参数
     */
    private final List<SqlFuncNodeParam> params = new ArrayList<>();

    public SqlFuncNode(String funcName) {
        this.funcName = funcName;
    }

    public SqlFuncNode copy() {
        SqlFuncNode sqlFuncNode = new SqlFuncNode(this.funcName);
        for (SqlFuncNodeParam param : params) {
            sqlFuncNode.getParams().add(param.copy());
        }
        return sqlFuncNode;
    }

    /**
     * 递归读取sql变量
     */
    public LinkedHashMap<String, Object> getSqlVariable() {
        LinkedHashMap<String, Object> sqlVariable = new LinkedHashMap<>();
        for (SqlFuncNodeParam param : params) {
            if (SqlFuncNodeParamEnum.SQL_FUNC.equals(param.getType())) {
                SqlFuncNode childSqlFunc = param.getFunc();
                sqlVariable.putAll(childSqlFunc.getSqlVariable());
            } else {
                SqlFuncParam sqlFuncParam = param.getParam();
                if (sqlFuncParam.isVariable()) {
                    String name = sqlFuncParam.getName();
                    Object value = sqlFuncParam.getValue();
                    sqlVariable.put(name, value);
                }
            }
        }
        return sqlVariable;
    }

    /**
     * 转换后的sql函数代码
     */
    public String toSql() {
        return "";
    }
}
