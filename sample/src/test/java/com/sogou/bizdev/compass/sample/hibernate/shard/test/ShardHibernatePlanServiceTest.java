package com.sogou.bizdev.compass.sample.hibernate.shard.test;

import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.shard.service.ShardHibernatePlanService;

@ContextConfiguration(locations = {"classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml","classpath*:/conf/hibernate/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml"})
public class ShardHibernatePlanServiceTest extends AbstractJUnit4SpringContextTests {
	
	private  final static Long ACCOUNTID=428937L;
	private static Long newPlanId=null;
	private final static Integer start=65535;

	
	@Test
	public void testCreatePlan() {
		ShardHibernatePlanService shardHibernatePlanService = (ShardHibernatePlanService)applicationContext.getBean("shardHibernatePlanService", ShardHibernatePlanService.class);
		Plan plan = createPlan();
		shardHibernatePlanService.createPlan(ACCOUNTID, plan);
		
		Plan plan1 = shardHibernatePlanService.getPlanById(ACCOUNTID, plan.getPlanId());
		newPlanId=plan.getPlanId();
		Assert.assertTrue(plan1!=null); 
	}
	
	@Test
	public void testGetPlanById() {
		ShardHibernatePlanService shardHibernatePlanService = (ShardHibernatePlanService)applicationContext.getBean("shardHibernatePlanService", ShardHibernatePlanService.class);
		Plan plan = shardHibernatePlanService.getPlanById(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getPlanId().longValue()==newPlanId);
 	}
	
	public Plan createPlan(){
		Plan plan=new Plan();
 		plan.setPlanId(new Random().nextInt(start)+100000000L);

		plan.setAccountId(ACCOUNTID);
 		plan.setCreateDate(new Date());
		plan.setName("myplan"+System.currentTimeMillis());
		return plan;
		
	}
	
	
	 
}
