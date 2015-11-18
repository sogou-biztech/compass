package com.sogou.bizdev.compass.sample.mybatis.shard.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.jdbctemplate.sequence.SequenceGenerator;
import com.sogou.bizdev.compass.sample.mybatis.shard.service.ShardMybatisPlanService;

@ContextConfiguration(locations = { "classpath*:/conf/mybatis/test-shard-*.xml","classpath*:test-shard-*.xml","classpath*:/conf/jdbctemplate/test-sequence-*.xml","classpath*:test-sequence-*.xml"})
public class ShardMybatisPlanServiceTest extends AbstractJUnit4SpringContextTests {
	private  final static Long ACCOUNTID=428937L;
	private  final static String PLAN_SEQUENCE="CPCPLAN_SEQ"; 
	private static Long newPlanId=null;
	
	@Test
	public void testInsertPlan() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan=createPlan();
		shardMybatisPlanService.insert(ACCOUNTID,plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getCpcplanid();
 	}
	
	@Test
	public void testGetPlanByPlanId() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getCpcplanid().longValue()==newPlanId);
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
		plan.setIspause(1);
		plan.setName("1myplan"+System.currentTimeMillis());
 		shardMybatisPlanService.update(ACCOUNTID,plan);
 		plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan!=null);
		Assert.assertTrue(plan.getIspause()==1);
		Assert.assertTrue(plan.getName().startsWith("1myplan"));
 	}
	
	@Test
	public void testDeletePlan() {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		 
 		shardMybatisPlanService.delete(ACCOUNTID,newPlanId);
 		Plan plan = shardMybatisPlanService.queryPlanByPlanId(ACCOUNTID, newPlanId);
		Assert.assertTrue(plan==null);
  	}
	 
 	public Plan createPlan(){
		Plan plan=new Plan();
		SequenceGenerator sequenceGenerator = (SequenceGenerator)applicationContext.getBean("sequenceGenerator", SequenceGenerator.class);
		plan.setCpcplanid(sequenceGenerator.getSequence(PLAN_SEQUENCE));

		plan.setAccountid(428937L);
		plan.setChgdate(new Date());
		plan.setCreatedate(new Date());
		plan.setEndDate(new Date());
		plan.setIspause(0);
		plan.setName("myplan"+System.currentTimeMillis());
		plan.setStartDate(new Date());
		return plan;
		
	}
	
	
	@Test
	public void testInsertPlanWithUnvaliableDataSource() throws InterruptedException {
		ShardMybatisPlanService shardMybatisPlanService = (ShardMybatisPlanService)applicationContext.getBean("shardMybatisPlanService", ShardMybatisPlanService.class);
		
		Plan plan=createPlan();
		//1.构造shard08库异常，插入该库plan
		try {
			shardMybatisPlanService.insert(3L,plan);
		} catch (Exception e) {
			//此处必抛出异常
			System.out.println("dev.dubhe08.mysql config error,invoke fail "+e);
		}
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getCpcplanid();
		//执行失败
 		
		//2.插入正常库plan
		plan=createPlan();
		shardMybatisPlanService.insert(6L,plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getCpcplanid();
		//执行成功
		
		//3.恢复shard08库构造，线程阻塞一段时间，然后执行插入该库plan
		System.out.println("start thread sleep 20s");
		Thread.sleep(20000);
 		plan=createPlan();
		shardMybatisPlanService.insert(3L,plan);
		Assert.assertTrue(plan!=null);
		newPlanId=plan.getCpcplanid();
		//执行成功
 	}
	 
}
