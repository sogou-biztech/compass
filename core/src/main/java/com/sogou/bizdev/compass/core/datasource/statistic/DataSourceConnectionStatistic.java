package com.sogou.bizdev.compass.core.datasource.statistic;

/**
 * class <code>DataSourceConnectionStatistic</code> provide datasource connection busy/idle/max numbers
 *
 * @author gly
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataSourceConnectionStatistic {
    /**
     * 数据源标识
     */
    private String datasourceId;

    /**
     * 是否存活
     */
    private boolean isAlive;

    /**
     * 数据库连接个数
     */
    private int connectionsNumber;

    /**
     * 数据库空闲连接个数
     */
    private int idleConnectionsNumber;

    /**
     * 数据库繁忙连接个数
     */
    private int busyConnectionsNumber;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public int getConnectionsNumber() {
        return connectionsNumber;
    }

    public void setConnectionsNumber(int connectionsNumber) {
        this.connectionsNumber = connectionsNumber;
    }

    public int getIdleConnectionsNumber() {
        return idleConnectionsNumber;
    }

    public void setIdleConnectionsNumber(int idleConnectionsNumber) {
        this.idleConnectionsNumber = idleConnectionsNumber;
    }

    public int getBusyConnectionsNumber() {
        return busyConnectionsNumber;
    }

    public void setBusyConnectionsNumber(int busyConnectionsNumber) {
        this.busyConnectionsNumber = busyConnectionsNumber;
    }

    @Override
    public String toString() {
        return "DataSourceConnectionStatistic [datasourceId=" + datasourceId
            + ", isAlive=" + isAlive
            + ", connectionsNumber=" + connectionsNumber
            + ", idleConnectionsNumber=" + idleConnectionsNumber
            + ", busyConnectionsNumber=" + busyConnectionsNumber
            + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + busyConnectionsNumber;
        result = prime * result + connectionsNumber;
        result = prime * result + ((datasourceId == null) ? 0 : datasourceId.hashCode());
        result = prime * result + idleConnectionsNumber;
        result = prime * result + (isAlive ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        DataSourceConnectionStatistic other = (DataSourceConnectionStatistic) obj;
        if (busyConnectionsNumber != other.busyConnectionsNumber) { return false; }
        if (connectionsNumber != other.connectionsNumber) { return false; }
        if (datasourceId == null) {
            if (other.datasourceId != null) { return false; }
        } else if (!datasourceId.equals(other.datasourceId)) { return false; }
        if (idleConnectionsNumber != other.idleConnectionsNumber) { return false; }
        if (isAlive != other.isAlive) { return false; }
        return true;
    }

}
