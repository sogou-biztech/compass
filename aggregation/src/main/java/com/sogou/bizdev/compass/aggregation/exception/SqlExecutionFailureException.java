package com.sogou.bizdev.compass.aggregation.exception;

/**
 * 此类用于描述与数据库交互时发生的异常
 *
 * @author yk
 * @since 1.0.0
 */
public class SqlExecutionFailureException extends RuntimeException {

    private static final long serialVersionUID = 7396342123160027703L;

    public SqlExecutionFailureException(String msg) {
        super(msg);
    }

    public SqlExecutionFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SqlExecutionFailureException(Throwable cause) {
        super(cause);
    }

}
