package com.sogou.bizdev.compass.core.datasource.retriever;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.sogou.bizdev.compass.core.datasource.DatabaseType;
import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;

/**
 * 数据库配置信息获取(e.g. C3P0 DataSource)
 * for heart beat monitoring.
 *
 * @author gly
 * @since 1.0.0
 */
public abstract class DatabasePropertiesRetriever {

    protected static final int ILLEGAL_NUMBER = -1;
    protected static final int UNALIVE_NUMBER = 0;

    protected static final Map<String, DatabaseType> DATABASE_TYPE_BY_DRIVER_CLASS = new HashMap<String, DatabaseType>();

    static {
        DATABASE_TYPE_BY_DRIVER_CLASS.put("com.mysql.jdbc.Driver", DatabaseType.MYSQL);
        DATABASE_TYPE_BY_DRIVER_CLASS.put("org.gjt.mm.mysql.Driver", DatabaseType.MYSQL);
        DATABASE_TYPE_BY_DRIVER_CLASS.put("com.mysql.jdbc.ReplicationDriver", DatabaseType.MYSQL);
        DATABASE_TYPE_BY_DRIVER_CLASS.put("oracle.jdbc.driver.OracleDriver", DatabaseType.ORACLE);
        DATABASE_TYPE_BY_DRIVER_CLASS.put("oracle.jdbc.OracleDriver", DatabaseType.ORACLE);
    }

    /**
     * 获取数据库连接Url
     *
     * @param dataSource
     * @return
     */
    public abstract String getDatabaseUrl(DataSource dataSource);

    /**
     * 获取数据库用户名
     *
     * @param dataSource
     * @return
     */
    public abstract String getUsername(DataSource dataSource);

    /**
     * 获取数据库密码
     *
     * @param dataSource
     * @return
     */
    public abstract String getPassword(DataSource dataSource);

    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     */
    public abstract DatabaseType getDatabaseType(DataSource dataSource);

    /**
     * 获取连接数
     *
     * @param dataSource
     * @return
     */
    public abstract int getConnectionsNumber(DataSource dataSource);

    /**
     * 获取空闲连接数
     *
     * @param dataSource
     * @return
     */
    public abstract int getIdleConnectionsNumber(DataSource dataSource);

    /**
     * 获取繁忙连接数
     *
     * @param dataSource
     * @return
     */
    public abstract int getBusyConnectionsNumber(DataSource dataSource);

    /**
     * 获取最大连接数
     *
     * @param dataSource
     * @return
     */
    public abstract int getMaxConnectionsNumber(DataSource dataSource);

    public DataSourceConnectionStatistic getDataSourceConnectionStatistic(String dataSourceId, DataSource dataSource) {
        DataSourceConnectionStatistic dataSourceConnectionStatistic = new DataSourceConnectionStatistic();
        dataSourceConnectionStatistic.setDatasourceId(dataSourceId);
        dataSourceConnectionStatistic.setConnectionsNumber(getConnectionsNumber(dataSource));
        dataSourceConnectionStatistic.setIdleConnectionsNumber(getIdleConnectionsNumber(dataSource));
        dataSourceConnectionStatistic.setBusyConnectionsNumber(getBusyConnectionsNumber(dataSource));
        // 当获取的连接数为0或者在获取过程中出现异常，这两种情况出现其一将视为unalive
        dataSourceConnectionStatistic.setAlive(getConnectionsNumber(dataSource) != UNALIVE_NUMBER &&
            (getConnectionsNumber(dataSource) != ILLEGAL_NUMBER
                && getIdleConnectionsNumber(dataSource) != ILLEGAL_NUMBER
                && getBusyConnectionsNumber(dataSource) != ILLEGAL_NUMBER));
        return dataSourceConnectionStatistic;
    }
}
