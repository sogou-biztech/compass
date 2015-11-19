package com.sogou.bizdev.compass.sample.hibernate.combined.test;

import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.combined.MSFirstService;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;
import com.sogou.bizdev.compass.sample.hibernate.shard.service.ShardHibernatePlanService;

/**
 * 多套库的情况
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml","/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml" })
public class MSFirstServiceTest extends AbstractJUnit4SpringContextTests{
	private static Long newAccountId=null;
	private final static Integer start=65535;
	private static Long newPlanId=null;

	
	@Test
	public void testInsert() {
		HibernateAccountService accountService = (HibernateAccountService)applicationContext.getBean("hibernateAccountService");
		Account account = createAccount();
		accountService.createAccount(account);
		
		
		Account account2 = accountService.getAccountById(account.getAccountId());
		newAccountId=account.getAccountId();
		Assert.assertTrue(account2!=null); 
	}
	
	@Test
	public void testCreatePlan() {
		ShardHibernatePlanService shardHibernatePlanService = (ShardHibernatePlanService)applicationContext.getBean("shardHibernatePlanService", ShardHibernatePlanService.class);
		Plan plan = createPlan();
		shardHibernatePlanService.createPlan(newAccountId, plan);
		
		Plan plan1 = shardHibernatePlanService.getPlanById(newAccountId, plan.getPlanId());
		newPlanId=plan.getPlanId();
		Assert.assertTrue(plan1!=null); 
	}
	
	@Test
	public void testGetMSAndShardPlan(){
		MSFirstService shardFirstService = (MSFirstService)applicationContext.getBean("mSFirstService");
		Plan plan = shardFirstService.getMSAndShardPlan(newAccountId, newPlanId);
		Assert.assertTrue(plan!=null); 
	}

	public Account createAccount(){
		Account account=new Account();
		account.setAccountId(new Random().nextInt(start)+100000000L);

		account.setEmail("xxx@sogou.com");
		account.setPassword("xxxxxx");
		account.setRegistDate(new Date()); 
		return account;
		
	}
	public Plan createPlan(){
		Plan plan=new Plan();
 		plan.setPlanId(new Random().nextInt(start)+100000000L);

		plan.setAccountId(newAccountId);
 		plan.setCreateDate(new Date());
		plan.setName("myplan"+System.currentTimeMillis());
		return plan;
		
	}
}
