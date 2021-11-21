package org.clever.dynamic.sql.dialect.func;

import org.clever.dynamic.sql.dialect.*;
import org.clever.dynamic.sql.dialect.exception.SqlFuncTransformException;

import java.util.Collection;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 22:33 <br/>
 */
public class JoinFuncTransform implements SqlFuncTransform {
    public static final String FUNC_NAME = "join";

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }

    @Override
    public boolean isSupport(DbType dbType) {
        return true;
    }

    @Override
    public SqlFuncNode transform(DbType dbType, SqlFuncNode src) {
        // join(java_arr)
        final String funcName = src.getFuncName();
        final List<SqlFuncNodeParam> params = src.getParams();
        if (params == null || params.size() != 1) {
            throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
        }
        final SqlFuncNodeParam param = params.get(0);
        if (!SqlFuncNodeParamEnum.SQL_PARAM.equals(param.getType())) {
            throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
        }
        // (#{_item_1}, #{_item_2}, #{_item_3}, #{_item_4}, #{_item_5}, ...)
        final String literal = param.getParam().getLiteral();
        final String name = param.getParam().getName();
        final Object value = param.getParam().getValue();
        final SqlFuncNode target = new SqlFuncNode(null);
        target.setParen(false);
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            if (collection.isEmpty()) {
                throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
            }
            int idx = 0;
            for (Object item : collection) {
                idx++;
                addSqlFuncParam(target, idx, literal, name, item);
            }
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            if (array.length <= 0) {
                throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
            }
            int idx = 0;
            for (Object item : array) {
                idx++;
                addSqlFuncParam(target, idx, literal, name, item);
            }
        } else {
            throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
        }
        return target;
    }

    private void addSqlFuncParam(SqlFuncNode target, int idx, String literal, String name, Object item) {
        SqlFuncParam sqlFuncParam = new SqlFuncParam(
                true,
                String.format("%s[%s]", literal, idx),
                String.format("%s_item_%s", name, idx),
                item
        );
        target.getParams().add(new SqlFuncNodeParam(sqlFuncParam));
    }
}
