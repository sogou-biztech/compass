package com.sogou.bizdev.compass.core.datasource.retriever;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizdev.compass.core.datasource.DatabaseType;

/**
 * 数据库配置信息获取(基于DBCP)
 * Only support standard Mysql and Oracle driver.
 *
 * @author gly
 * @version 1.0.0
 * @since 1.0.0
 */
public class DbcpDatabasePropertiesRetriever extends DatabasePropertiesRetriever {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getDatabaseUrl(DataSource dataSource) {
        BasicDataSource ds = (BasicDataSource) dataSource;
        return ds.getUrl();
    }

    @Override
    public String getUsername(DataSource dataSource) {
        BasicDataSource ds = (BasicDataSource) dataSource;
        return ds.getUsername();
    }

    @Override
    public String getPassword(DataSource dataSource) {
        BasicDataSource ds = (BasicDataSource) dataSource;
        return ds.getPassword();
    }

    @Override
    public DatabaseType getDatabaseType(DataSource dataSource) {
        BasicDataSource ds = (BasicDataSource) dataSource;
        String driverClass = ds.getDriverClassName();
        DatabaseType dbType = DATABASE_TYPE_BY_DRIVER_CLASS.get(driverClass);
        if (null == dbType) {
            throw new IllegalArgumentException("Unable to find database type using driver class '" + driverClass + "'");
        }
        return dbType;
    }

    @Override
    public int getConnectionsNumber(DataSource dataSource) {
        try {
            BasicDataSource ds = (BasicDataSource) dataSource;
            return ds.getNumActive();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query DBCP datasource={} connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getIdleConnectionsNumber(DataSource dataSource) {
        try {
            BasicDataSource ds = (BasicDataSource) dataSource;
            return ds.getNumIdle();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query DBCP datasource={} idle connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getBusyConnectionsNumber(DataSource dataSource) {
        try {
            BasicDataSource ds = (BasicDataSource) dataSource;
            return ds.getNumActive();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("query DBCP datasource={} busy connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

    @Override
    public int getMaxConnectionsNumber(DataSource dataSource) {
        try {
            BasicDataSource ds = (BasicDataSource) dataSource;
            return ds.getMaxActive();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.info("query DBCP datasource={} max connections number failed", dataSource);
            }
            return ILLEGAL_NUMBER;
        }
    }

}
