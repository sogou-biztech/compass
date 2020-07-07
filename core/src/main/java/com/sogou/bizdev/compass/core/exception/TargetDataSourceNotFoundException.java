package com.sogou.bizdev.compass.core.exception;

/**
 * 无法定位目的数据源异常，可能是分库依据缺失或者是主从库的配置缺失
 *
 * @author cl
 * @since 1.0.0
 */
public class TargetDataSourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8095644888733693442L;

    public TargetDataSourceNotFoundException() {
        super();

    }

    public TargetDataSourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetDataSourceNotFoundException(String message) {
        super(message);
    }

    public TargetDataSourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
