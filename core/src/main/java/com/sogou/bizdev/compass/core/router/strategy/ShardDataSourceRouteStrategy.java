package com.sogou.bizdev.compass.core.router.strategy;

import java.util.List;

import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSourceContext;
import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * 数据源分库路由策略，用于用户扩展
 * 支持分库之后再分表
 * @author xr
 * @since 1.0.0
 */
public interface ShardDataSourceRouteStrategy 
{


	/**
	 * 根据路由标识返分库id，id对应分库上配置的id
	 * @param routeKey 路由标识
	 * @param shardDataSourceContext  ShardDataSource的用户视图
	 * @return 分库id，对应 {@link ShardDataSource}的shardDataSourceId
	 */
	public TableContext route(Object routeKey, ShardDataSourceContext shardDataSourceContext);

	/**
	 * 列举出所有的库表id，用于数据聚合层查询时进行sql替换
	 * @param masterSlaveDataSourceIds
	 * @return
	 */
	public List<TableContext> getMasterSlaveDataSourceTableContexts(String masterSlaveDataSourceId,ShardDataSourceContext shardDataSourceContext);
	

}
