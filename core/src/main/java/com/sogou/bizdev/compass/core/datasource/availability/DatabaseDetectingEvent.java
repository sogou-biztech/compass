package com.sogou.bizdev.compass.core.datasource.availability;

import com.sogou.bizdev.compass.core.datasource.DatabaseType;
import com.sogou.bizdev.compass.core.datasource.SingleDataSource;

/**
 * 心跳探测事件
 *
 * @author gly
 * @since 1.0.0
 */
public class DatabaseDetectingEvent {

    /**
     * 需探测的数据源
     */
    private SingleDataSource dataSource;

    /**
     * 心跳探测Sql语句
     */
    private String pingSql;

    /**
     * 数据库类型
     */
    private DatabaseType databaseType;

    public DatabaseDetectingEvent(SingleDataSource dataSource, DatabaseType databaseType, String pingSql) {
        this.dataSource = dataSource;
        this.databaseType = databaseType;
        this.pingSql = pingSql;
    }

    public SingleDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(SingleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getPingSql() {
        return pingSql;
    }

    public void setPingSql(String pingSql) {
        this.pingSql = pingSql;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public String toString() {
        return "DatabaseDetectingEvent [dataSource=" + dataSource
            + ", pingSql=" + pingSql
            + ", databaseType=" + databaseType
            + "]";
    }

}
