package com.sogou.bizdev.compass.core.exception;

import org.springframework.dao.DataAccessException;

/**
 * 数据源挂掉，导致无法选出可用数据源的异常
 *
 * @author gly
 * @since 1.0.0
 */
public class NoDataSourceAvailableException extends DataAccessException {

    private static final long serialVersionUID = -7470562178445345504L;

    public NoDataSourceAvailableException() {
        this("No datasource available");
    }

    public NoDataSourceAvailableException(String message) {
        super(message);
    }

    public NoDataSourceAvailableException(Throwable cause) {
        this("No datasource available", cause);
    }

    public NoDataSourceAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
