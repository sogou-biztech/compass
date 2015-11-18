/**
 * 
 */
package com.sogou.bizdev.compass.core.exception;

/** 
 * @author zjc
 * @since 1.0.0 
 */
public class RouteKeyInconsistencyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7960035268651501187L;

	public RouteKeyInconsistencyException() {
		super();
	}

	public RouteKeyInconsistencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public RouteKeyInconsistencyException(String message) {
		super(message);
	}

	public RouteKeyInconsistencyException(Throwable cause) {
		super(cause);
	}

}
