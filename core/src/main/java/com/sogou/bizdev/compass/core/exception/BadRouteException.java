package com.sogou.bizdev.compass.core.exception;

/**
 * 不正确的路由异常
 * 
 * @author xr
 * @since 1.0.0
 */
public class BadRouteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7960035268651501187L;

	public BadRouteException() {
		super();
	}

	public BadRouteException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadRouteException(String message) {
		super(message);
	}

	public BadRouteException(Throwable cause) {
		super(cause);
	}

}
