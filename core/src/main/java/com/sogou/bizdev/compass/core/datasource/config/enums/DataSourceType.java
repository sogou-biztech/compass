package com.sogou.bizdev.compass.core.datasource.config.enums;

/**
 * 数据源类型
 *
 * @author gly
 * @version 1.0.0
 * @since 1.0.0
 */
public enum DataSourceType {
    SHARD_DATASOURCE("shardDatasource"),
    MASTER_SLAVE_DATASOURCE("masterSlaveDatasource"),
    NONE("none");

    String value;

    public String getValue() {
        return value;
    }

    private DataSourceType(String value) {
        this.value = value;
    }

    public static DataSourceType parse(String i) {
        for (DataSourceType c : DataSourceType.values()) {
            if (c.getValue().equals(i)) { return c; }
        }
        return null;
    }

}
