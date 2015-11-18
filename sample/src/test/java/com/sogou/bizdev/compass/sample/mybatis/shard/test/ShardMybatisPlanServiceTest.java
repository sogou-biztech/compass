package com.sogou.bizdev.compass.sample.mybatis.shard.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.mybatis.shard.service.ShardMybatisPlanService;

@ContextConfiguration(locations = { "classpath*:/conf/mybatis/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml"})
public class ShardMybatisPlanServiceTest extends AbstractJUnit4SpringContextTests {
	private  final static Long ACCOUNTID=428937L;
	private static Long newPlanId=null;
	private final static Integer start=65535;

	
	@Test
	public void testInsertPlan() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan=createPlan();
		shardMybatisPlanService.createPlan(ACCOUNTID,plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getPlanId();
 	}
	
	@Test
	public void testGetPlanByPlanId() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getPlanId().longValue()==newPlanId);
	}
	
	@Test
	public void testGetPlansByPlanIds() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		List<Long> planIds=new ArrayList<Long>();
		planIds.add(82269L);
		planIds.add(82268L);
		planIds.add(82267L);
		planIds.add(newPlanId);
		List<Plan> plans = shardMybatisPlanService.queryPlansByPlanIds(ACCOUNTID, planIds);
		Assert.assertTrue(plans!=null);
		Assert.assertTrue(plans.size()==1);
	}
	
	@Test
	public void testUpdatePlan() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		plan.setName("1myplan"+System.currentTimeMillis());
 		shardMybatisPlanService.updatePlan(ACCOUNTID,plan);
 		plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getName().startsWith("1myplan"));
 	}
	
	@Test
	public void testDeletePlan() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		 
 		shardMybatisPlanService.deletePlan(ACCOUNTID,newPlanId);
 		Plan plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan==null);
  	}
	 
 	public Plan createPlan(){
		Plan plan=new Plan();
 		plan.setPlanId(new Random().nextInt(start)+100000000L);

		plan.setAccountId(ACCOUNTID);
 		plan.setCreateDate(new Date());
		plan.setName("myplan"+System.currentTimeMillis());
		return plan;
		
	}
	
	
	@Test
	public void testInsertPlanWithUnvaliableDataSource() throws InterruptedException {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan=createPlan();
		boolean error=false;
		//1.构造shard02库异常，插入该库plan
		try {
			shardMybatisPlanService.createPlan(ACCOUNTID,plan);
		} catch (Exception e) {
			//此处必抛出异常
			System.out.println("dev.dubhe08.mysql config error,invoke fail "+e);
			error=true;
		}
		Assert.assertTrue(error);
		 
		
		//3.恢复shard02库构造，线程阻塞一段时间，然后执行插入该库plan
		System.out.println("start thread sleep 20s");
		Thread.sleep(20000);
 		plan=createPlan();
		shardMybatisPlanService.createPlan(ACCOUNTID,plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getPlanId();
		//执行成功
 	}
	 
}
