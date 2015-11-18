package com.sogou.bizdev.compass.core.router.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sogou.bizdev.compass.core.preprocessor.RouteContext;
import com.sogou.bizdev.compass.core.router.ShardDataSourceRouter;
import com.sogou.bizdev.compass.core.router.strategy.impl.ModShardDataSourceRouteStrategy;

public class ShardDataSourceRouterImplTest {
	
	String dataSourceId = "adcpc";
	
	
	private void before(){
		RouteContext routingContext = new RouteContext(null, 274280L, false);
		routingContext.bindToThread(dataSourceId);
	}
	
	private void after(){
		RouteContext.removeRoutingContext(dataSourceId);
	}
		
	@Test
	public void testGetShardDataSourceId(){
		before();
		List<String> groupDataSourceIds = new ArrayList<String>();
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
		//String dbId = shardDataSourceRouter.getMasterSlaveDataSourceId(dataSourceId, groupDataSourceIds);
		//Assert.assertEquals(dbId, "adcpc_10");
		after();
	}
}
