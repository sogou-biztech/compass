package com.sogou.bizdev.compass.sample.hibernate.shard.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.shard.service.ShardHibernatePlanService;

@ContextConfiguration(locations = {"classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:test-masterslave-*.xml", "classpath*:/conf/hibernate/test-shard-*.xml","classpath*:test-shard-*.xml" })
public class ShardPlanServiceTest extends AbstractJUnit4SpringContextTests {
	
	@Test
	public void testGetPlanById() {
		ShardHibernatePlanService shardHibernatePlanService = (ShardHibernatePlanService)applicationContext.getBean("shardHibernatePlanService", ShardHibernatePlanService.class);
		Plan plan = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getCpcplanid().longValue()==99999999L);
		System.out.println(plan);
	}
	
	@Test
	public void testCreatePlan() {
		ShardHibernatePlanService shardHibernatePlanService = (ShardHibernatePlanService)applicationContext.getBean("shardHibernatePlanService", ShardHibernatePlanService.class);
		Plan plan = new Plan();
		plan.setCpcplanid(999999999L);
		plan.setName("testplan1");
		plan.setAccountid(375832L);
		plan.setIspause(0);
		plan.setCreatedate(new Date());
		plan.setChgdate(new Date());
		plan.setStartDate(new Date());
		plan.setEndDate(new Date());
		shardHibernatePlanService.createPlan(375832L, plan);
		
		Plan plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getCpcplanid().longValue()==999999999);
		plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		plan1 = shardHibernatePlanService.getPlanById(375832L, 99999999L);
		System.out.println(plan1);
	}
	
	 
}
