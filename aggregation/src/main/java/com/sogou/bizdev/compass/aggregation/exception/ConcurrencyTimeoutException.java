package com.sogou.bizdev.compass.aggregation.exception;

/**
 * 此类用于描述多线程并发执行时的超时异常
 * 
 * @author yk
 * @since 1.0.0
 */
public class ConcurrencyTimeoutException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2059678352535691563L;
	
	public ConcurrencyTimeoutException(String msg) {
		super(msg);
	}
	
	public ConcurrencyTimeoutException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ConcurrencyTimeoutException(Throwable cause) {
		super(cause);
	}

}
