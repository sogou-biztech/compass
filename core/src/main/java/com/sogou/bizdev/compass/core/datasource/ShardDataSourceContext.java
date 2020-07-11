package com.sogou.bizdev.compass.core.datasource;

import java.util.List;
import java.util.Map;

/**
 * 提供给用户使用的ShardDataSource视图
 *
 * @author cl
 */
public interface ShardDataSourceContext {
    public List<String> getMasterSlaveDataSourceIds();

    public Map<String, MasterSlaveDataSource> getIdToMasterSlaveDataSource();

    public String getShardDataSourceId();
}
