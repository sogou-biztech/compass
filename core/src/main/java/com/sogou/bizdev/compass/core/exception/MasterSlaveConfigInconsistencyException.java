/**
 * 
 */
package com.sogou.bizdev.compass.core.exception;

/** 
 * @author zjc
 * @since 1.0.0 
 */
public class MasterSlaveConfigInconsistencyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2964322447263828181L;

	/**
	 * 
	 */
	public MasterSlaveConfigInconsistencyException() {
	}

	/**
	 * @param message
	 */
	public MasterSlaveConfigInconsistencyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MasterSlaveConfigInconsistencyException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MasterSlaveConfigInconsistencyException(String message, Throwable cause) {
		super(message, cause);
	}

}
