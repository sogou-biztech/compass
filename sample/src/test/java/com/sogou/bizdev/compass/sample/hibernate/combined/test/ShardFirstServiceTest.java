package com.sogou.bizdev.compass.sample.hibernate.combined.test;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.combined.ShardFirstService;

/**
 * 多套库的情况
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-shard-*.xml","classpath*:test-shard-*.xml","/conf/hibernate/test-masterslave-*.xml","classpath*:test-masterslave-*.xml" })
public class ShardFirstServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void testGetMSAndShardPlan(){
		ShardFirstService shardFirstService = (ShardFirstService)applicationContext.getBean("shardFirstService");
		Plan plan = shardFirstService.getMSAndShardPlan(375832L, 99999999L);

		System.out.println(plan);
	}

	
}
