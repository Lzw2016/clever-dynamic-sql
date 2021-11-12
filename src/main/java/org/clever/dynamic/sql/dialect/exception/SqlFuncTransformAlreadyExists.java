package org.clever.dynamic.sql.dialect.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/11/12 19:56 <br/>
 */
public class SqlFuncTransformAlreadyExists extends RuntimeException {

    public SqlFuncTransformAlreadyExists() {
        super();
    }

    public SqlFuncTransformAlreadyExists(String message) {
        super(message);
    }

    public SqlFuncTransformAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlFuncTransformAlreadyExists(Throwable cause) {
        super(cause);
    }
}
