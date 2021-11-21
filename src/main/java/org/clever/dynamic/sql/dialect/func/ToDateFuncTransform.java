package org.clever.dynamic.sql.dialect.func;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.dialect.*;
import org.clever.dynamic.sql.dialect.exception.SqlFuncTransformException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/11 17:46 <br/>
 */
@Slf4j
public class ToDateFuncTransform implements SqlFuncTransform {
    public static final String FUNC_NAME = "to_date";

    private static final Set<DbType> DB_TYPES = Collections.unmodifiableSet(new HashSet<DbType>() {{
        add(DbType.MYSQL);
        add(DbType.ORACLE);
        add(DbType.SQL_SERVER);
    }});

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }

    @Override
    public boolean isSupport(DbType dbType) {
        return DB_TYPES.contains(dbType);
    }

    @Override
    public SqlFuncNode transform(DbType dbType, SqlFuncNode src) {
        // to_date(param)
        final String funcName = src.getFuncName();
        final List<SqlFuncNodeParam> params = src.getParams();
        if (params == null || params.size() != 1) {
            throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
        }
        SqlFuncNodeParam sqlFuncNodeParam = params.get(0).copy();
        SqlFuncNode target;
        switch (dbType) {
            case MYSQL:
                target = new SqlFuncNode("#{%s}");
                target.getParams().add(sqlFuncNodeParam);
                break;
            case ORACLE:
                target = new SqlFuncNode("TO_DATE(#{%s}, 'YYYY-MM-DD')");
                target.getParams().add(sqlFuncNodeParam);
                SqlFuncParam sqlFuncParam = new SqlFuncParam(false, "'YYYY-MM-DD'", null, null);
                target.getParams().add(new SqlFuncNodeParam(sqlFuncParam));
                break;
            case SQL_SERVER:
                target = new SqlFuncNode("CONVERT(datetime, #{%s})");
                target.getParams().add(sqlFuncNodeParam);
                break;
            default:
                throw new SqlFuncTransformException("不支持的数据库:" + dbType);
        }
        return target;
    }
}
