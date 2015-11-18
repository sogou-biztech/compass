package com.sogou.bizdev.compass.sample.jdbctemplate.shard.test;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.jdbctemplate.shard.service.ShardPlanService;

@ContextConfiguration(locations = { "classpath*:/conf/jdbctemplate/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml" })
public class ShardPlanServiceTest extends AbstractJUnit4SpringContextTests {
	private  final static Long ACCOUNTID=428937L;
	private static Long newPlanId=null;
	private final static Integer start=65535;

	
	@Test
	public void testCreatePlanById() {
		ShardPlanService shardPlanService = (ShardPlanService)applicationContext.getBean("shardPlanService", ShardPlanService.class);
		Plan plan=createPlan();
		shardPlanService.createPlans(ACCOUNTID, plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getPlanId();
	}
	
	
	@Test
	public void testGetPlanById() {
		ShardPlanService shardPlanService = (ShardPlanService)applicationContext.getBean("shardPlanService", ShardPlanService.class);
		List<Plan> plans = shardPlanService.getPlansByAccountId(ACCOUNTID);
		Assert.assertTrue(plans!=null&&plans.size()!=0);
	}
	
	@Test
	public void testUpdatePlanById() {
		ShardPlanService shardPlanService = (ShardPlanService)applicationContext.getBean("shardPlanService", ShardPlanService.class);
		Plan p = new Plan();
		p.setAccountId(ACCOUNTID);
		p.setPlanId(newPlanId);
		p.setName("newplan"+System.currentTimeMillis());
		p=shardPlanService.updatePlan(ACCOUNTID, p);
		Assert.assertTrue(p.getName().startsWith("newplan"));
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
