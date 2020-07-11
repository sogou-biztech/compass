/**
 *
 */
package com.sogou.bizdev.compass.core.exception;

/**
 * @author zjc
 * @since 1.0.0
 */
public class MasterSlaveConfigInconsistencyException extends RuntimeException {

    private static final long serialVersionUID = 2964322447263828181L;

    public MasterSlaveConfigInconsistencyException() {
        super();
    }

    public MasterSlaveConfigInconsistencyException(String message) {
        super(message);
    }

    public MasterSlaveConfigInconsistencyException(Throwable cause) {
        super(cause);
    }

    public MasterSlaveConfigInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

}
