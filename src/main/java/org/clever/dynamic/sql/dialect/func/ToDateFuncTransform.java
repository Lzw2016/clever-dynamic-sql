package org.clever.dynamic.sql.dialect.func;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.sql.dialect.DbType;
import org.clever.dynamic.sql.dialect.SqlFuncNode;
import org.clever.dynamic.sql.dialect.SqlFuncNodeParam;
import org.clever.dynamic.sql.dialect.SqlFuncTransform;
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
    public SqlFuncNode transform(DbType dbType, SqlFuncNode sqlFuncNode) {
        // to_date(param)
        final String funcName = sqlFuncNode.getFuncName();
        final List<SqlFuncNodeParam> params = sqlFuncNode.getParams();
        if (params == null || params.size() != 1) {
            throw new SqlFuncTransformException("SQL函数" + funcName + "参数错误");
        }
        SqlFuncNodeParam sqlFuncNodeParam = params.get(0);

        SqlFuncNode resultFunc;
        switch (dbType) {
            case MYSQL:
                resultFunc = new SqlFuncNode("#{%s}");
                resultFunc.getParams().add(sqlFuncNodeParam);
                break;
            case ORACLE:
                resultFunc = new SqlFuncNode("TO_DATE(#{%s}, 'YYYY-MM-DD')");
                resultFunc.getParams().add(sqlFuncNodeParam);
                break;
            case SQL_SERVER:
                resultFunc = new SqlFuncNode("CONVERT(datetime, #{%s})");
                resultFunc.getParams().add(sqlFuncNodeParam);
                break;
            default:
                throw new SqlFuncTransformException("不支持的数据库:" + dbType);
        }
        return resultFunc;
    }
}
