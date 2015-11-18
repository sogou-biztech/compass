package com.sogou.bizdev.compass.sample.jdbctemplate.shard.test;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.jdbctemplate.shard.service.ShardPlanService;

@ContextConfiguration(locations = { "classpath*:/conf/jdbctemplate/test-shard-*.xml","classpath*:test-shard-*.xml" })
public class ShardPlanServiceTest extends AbstractJUnit4SpringContextTests {
	
	private ShardPlanService shardPlanService;

	public ShardPlanService getShardPlanService() {
		return shardPlanService;
	}

	public void setShardPlanService(ShardPlanService shardPlanService) {
		this.shardPlanService = shardPlanService;
	}
	
	private Long accountId = 375832L;

	@Test
	public void testGetPlanById() {
		ShardPlanService shardPlanService = (ShardPlanService)applicationContext.getBean("shardPlanService", ShardPlanService.class);
		List<Plan> plans = shardPlanService.getPlansById(375832L);
		System.err.println(plans);
	}
	
	@Test
	public void testUpdatePlanById() {
		ShardPlanService shardPlanService = (ShardPlanService)applicationContext.getBean("shardPlanService", ShardPlanService.class);
		Plan p = new Plan();
		Long planId = -1385028000567L;
		p.setAccountid(accountId);
		p.setCpcplanid(planId);
		p.setName("testshard");
		p.setIspause(1);
		System.err.println(shardPlanService.updatePlanById(accountId, p));
	}
	 
}
