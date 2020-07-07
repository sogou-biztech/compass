package com.sogou.bizdev.compass.core.exception;

/**
 * 路由标识不存在异常
 *
 * @author xr
 * @since 1.0.0
 */
public class RouteKeyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8843626118395192305L;

    public RouteKeyNotFoundException() {
        super();
    }

    public RouteKeyNotFoundException(String message) {
        super(message);
    }

    public RouteKeyNotFoundException(String message, Throwable t) {
        super(message, t);
    }

}
