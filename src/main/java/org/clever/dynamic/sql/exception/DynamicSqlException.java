package org.clever.dynamic.sql.exception;

public class DynamicSqlException extends RuntimeException {

    private static final long serialVersionUID = 3880206998166270511L;

    public DynamicSqlException() {
        super();
    }

    public DynamicSqlException(String message) {
        super(message);
    }

    public DynamicSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicSqlException(Throwable cause) {
        super(cause);
    }

}
