package com.sogou.bizdev.compass.core.exception;

/**
 * 主从标识不存在异常
 *
 * @author xr
 * @since 1.0.0
 */
public class MasterSlaveConfigNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 51729030571936233L;

    public MasterSlaveConfigNotFoundException() {
        super();
    }

    public MasterSlaveConfigNotFoundException(String message) {
        super(message);
    }

    public MasterSlaveConfigNotFoundException(String message, Throwable t) {
        super(message, t);
    }

}
