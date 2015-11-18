package com.sogou.bizdev.compass.core.router.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSourceContext;
import com.sogou.bizdev.compass.core.router.TableContext;
import com.sogou.bizdev.compass.core.router.strategy.impl.ModShardDataSourceRouteStrategy;

public class ModDataSourceRouterStrategyTest{
	
	ModShardDataSourceRouteStrategy modDataSourceRouterStrategy = new ModShardDataSourceRouteStrategy();

	@Test
	public void testRoute(){
		modDataSourceRouterStrategy.setTableCount(32);
		Long routeEvidence = 274280L;
		
		final List<String> groupDataSourceIds = new ArrayList<String>();
		groupDataSourceIds.add("adcpc_01");
		groupDataSourceIds.add("adcpc_02");
		groupDataSourceIds.add("adcpc_03");
		groupDataSourceIds.add("adcpc_04");
		groupDataSourceIds.add("adcpc_05");
		groupDataSourceIds.add("adcpc_06");
		groupDataSourceIds.add("adcpc_07");
		groupDataSourceIds.add("adcpc_08");
		groupDataSourceIds.add("adcpc_09");
		groupDataSourceIds.add("adcpc_10");
		
		ShardDataSourceContext shardDataSourceContext=new ShardDataSourceContext() {
			
			@Override
			public String getShardDataSourceId()
			{
				return "adcpc";
			}
			
			@Override
			public List<String> getMasterSlaveDataSourceIds()
			{
				return groupDataSourceIds;
			}
			
			@Override
			public Map<String, MasterSlaveDataSource> getIdToMasterSlaveDataSource()
			{
			
				return null;
			}
		};
		
		
		TableContext tableContext = modDataSourceRouterStrategy.route(routeEvidence,shardDataSourceContext);
		
		Assert.assertEquals(tableContext.getMasterSlaveDataSourceId(), "adcpc_01");    
	}
}
