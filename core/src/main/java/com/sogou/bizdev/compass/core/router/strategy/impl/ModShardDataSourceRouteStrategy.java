package com.sogou.bizdev.compass.core.router.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.core.datasource.ShardDataSourceContext;
import com.sogou.bizdev.compass.core.router.TableContext;
import com.sogou.bizdev.compass.core.router.strategy.ShardDataSourceRouteStrategy;

/**
 * 取模的数据源路由策略
 * 
 * @author xr
 * @since 1.0.0
 */
public class ModShardDataSourceRouteStrategy implements ShardDataSourceRouteStrategy
{
	/**
	 * 每一个分库下的分表数量
	 */
	private int tableCount;
	
	public int getTableCount() 
	{
		return tableCount;
	}

	public void setTableCount(int tableCount) 
	{
		if(tableCount <=0)
		{
			throw new IllegalArgumentException("tableCount:["+tableCount+"],must > 0!");
		}
		this.tableCount = tableCount;
	}

	@Override
	public TableContext route(Object routeKey, ShardDataSourceContext shardDataSourceContext)
	{
		List<String> masterSlaveDataSourceIds=shardDataSourceContext.getMasterSlaveDataSourceIds();
		if( CollectionUtils.isEmpty(masterSlaveDataSourceIds))
		{
			throw new IllegalArgumentException("ShardDataSource:["+shardDataSourceContext.getShardDataSourceId()+"] masterSlaveDataSourceIds is empty!");
		}
		
		if (routeKey == null) 
		{
			throw new IllegalArgumentException("null routeKey");
		}
		
		if( !(routeKey instanceof Long))
		{
			throw new IllegalArgumentException("bad type routeKey:["+routeKey+"],must be long!");
		}
		
		int masterSlaveDataSourceSize=masterSlaveDataSourceIds.size();
		long longRouteKey = (Long) routeKey;
		int index = (int)(longRouteKey % masterSlaveDataSourceSize);
		String selectedMasterSlaveDataSourceId=masterSlaveDataSourceIds.get(index);
		long dbId  = longRouteKey % masterSlaveDataSourceSize + 1;
		long tableId = (longRouteKey / masterSlaveDataSourceSize) % tableCount + 1;
		return new TableContext(selectedMasterSlaveDataSourceId,String.valueOf(dbId), String.valueOf(tableId));
	}
	
	
	


	@Override
	public List<TableContext> getMasterSlaveDataSourceTableContexts(String masterSlaveDataSourceId,ShardDataSourceContext shardDataSourceContext)
	{
		List<String> masterSlaveDataSourceIds=shardDataSourceContext.getMasterSlaveDataSourceIds();
		if( CollectionUtils.isEmpty(masterSlaveDataSourceIds))
		{
			throw new IllegalArgumentException("ShardDataSource:["+shardDataSourceContext.getShardDataSourceId()+"] masterSlaveDataSourceIds is empty!");
		}
		
		int masterSlaveDataSourceIndex=masterSlaveDataSourceIds.indexOf(masterSlaveDataSourceId);
		if(masterSlaveDataSourceIndex==-1)
		{
			throw new IllegalArgumentException("ShardDataSource:["+shardDataSourceContext.getShardDataSourceId()+"], masterSlaveDataSourceId:["+ 
					masterSlaveDataSourceId+"] is not in masterSlaveDataSourceIds:["+masterSlaveDataSourceIds+"]!");
		}
		
		int dbIndex=masterSlaveDataSourceIndex+1;
		
		ArrayList<TableContext> tableContexts=new ArrayList<TableContext>(); 
		
		for(int index=0;index < this.tableCount;index++)
		{
			int tableIndex=index+1;
			TableContext tableContext=new TableContext(masterSlaveDataSourceId,String.valueOf(dbIndex), String.valueOf(tableIndex));
			tableContexts.add(tableContext);
			
		}
		return tableContexts;
	}

	

}
