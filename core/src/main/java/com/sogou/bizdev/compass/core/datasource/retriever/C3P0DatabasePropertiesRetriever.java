package com.sogou.bizdev.compass.core.datasource.retriever;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sogou.bizdev.compass.core.datasource.DatabaseType;

/**
 * 数据库配置信息获取(基于C3P0)
 * Only support standard Mysql and Oracle driver.
 *
 * @author gly
 * @since 1.0.0
 */
public class C3P0DatabasePropertiesRetriever extends DatabasePropertiesRetriever {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getDatabaseUrl(DataSource dataSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
        return ds.getJdbcUrl();
    }

    @Override
    public String getUsername(DataSource dataSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
        return ds.getUser();
    }

    @Override
    public String getPassword(DataSource dataSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
        return ds.getPassword();
    }

    @Override
    public DatabaseType getDatabaseType(DataSource dataSource) {
        ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
        String driverClass = ds.getDriverClass();
        DatabaseType dbType = DATABASE_TYPE_BY_DRIVER_CLASS.get(driverClass);
        if (null == dbType) {
            throw new IllegalArgumentException("Unable to find database type using driver class '" + driverClass + "'");
        }
        return dbType;
    }

    @Override
    public int getConnectionsNumber(DataSource dataSource) {
        try {
            ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
            return ds.getNumConnectionsDefaultUser();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query C3P0 datasource={} connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getIdleConnectionsNumber(DataSource dataSource) {
        try {
            ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
            return ds.getNumIdleConnectionsDefaultUser();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query C3P0 datasource={} idle connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getBusyConnectionsNumber(DataSource dataSource) {
        try {
            ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
            return ds.getNumBusyConnectionsDefaultUser();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query C3P0 datasource={} busy connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getMaxConnectionsNumber(DataSource dataSource) {
        try {
            ComboPooledDataSource ds = (ComboPooledDataSource) dataSource;
            return ds.getMaxPoolSize();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query C3P0 datasource={} max connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

}
