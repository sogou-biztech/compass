package com.sogou.bizdev.compass.core.exception;

/**
 * 数据源未命中异常
 *
 * @author gly
 * @since 1.0.0
 */
public class NoConnectionAvailableException extends RuntimeException {

    private static final long serialVersionUID = -7470562178445345504L;

    public NoConnectionAvailableException() {
        this("No connection available");
    }

    public NoConnectionAvailableException(String message) {
        super(message);
    }

    public NoConnectionAvailableException(Throwable cause) {
        this("No connection available", cause);
    }

    public NoConnectionAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
