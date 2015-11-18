package com.sogou.bizdev.compass.sample.mybatis.masterslave.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.mybatis.masterslave.service.MybatisAccountService;

@ContextConfiguration(locations = { "classpath*:/conf/mybatis/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml"})
public class MasterSlaveMybatisAccountServiceTest extends AbstractJUnit4SpringContextTests {
	
	private static Long newAccountId=null;
	private final static Integer start=65535;

	
	
	@Test
	public void testInsertAccount() {
		MybatisAccountService mybatisAccountService = (MybatisAccountService)applicationContext.getBean("mybatisAccountService", MybatisAccountService.class);
		
		Account account=createAccount();
		mybatisAccountService.createAccount(account);
		Assert.assertTrue(account!=null);
		newAccountId=account.getAccountId();
 	}
	
	@Test
	public void testGetAccountById() {
		MybatisAccountService accountService = (MybatisAccountService)applicationContext.getBean("mybatisAccountService", MybatisAccountService.class);
		Account account = accountService.getAccountByAccountId(newAccountId);
		Assert.assertTrue(account!=null);
		Assert.assertTrue(account.getAccountId().longValue()==newAccountId);
	}
	
	@Test
	public void testGetAccountsByIds() {
		MybatisAccountService accountService = (MybatisAccountService)applicationContext.getBean("mybatisAccountService", MybatisAccountService.class);
		List<Long> accountIds=new ArrayList<Long>();
		accountIds.add(82269L);
		accountIds.add(82268L);
		accountIds.add(82267L);
		accountIds.add(newAccountId);
		
		List<Account> accountForTests = accountService.getAccountsByAccountIds(accountIds);
		Assert.assertTrue(accountForTests!=null);
		Assert.assertTrue(accountForTests.size()==1);
	}
	
	@Test
	public void testUpdateAccount() {
		MybatisAccountService accountService = (MybatisAccountService)applicationContext.getBean("mybatisAccountService", MybatisAccountService.class);
		
		Account account =accountService.getAccountByAccountId(newAccountId);
		account.setEmail("xxx@sogou.com"+System.currentTimeMillis());
		account.setPassword(""+System.currentTimeMillis());
		account.setRegistDate(new Date());
		 
		accountService.updateAccount(account);
		account = accountService.getAccountByAccountId(newAccountId);
		Assert.assertTrue(account!=null);
 		Assert.assertTrue(account.getEmail().startsWith("xxx@sogou.com"));
  	}
	
	@Test
	public void testDeleteAccount() {
		MybatisAccountService accountService = (MybatisAccountService)applicationContext.getBean("mybatisAccountService", MybatisAccountService.class);
		
		 
		accountService.deleteAccount(newAccountId);
		Account account = accountService.getAccountByAccountId(newAccountId);
		Assert.assertTrue(account==null);
  	}
	 
	
	public Account createAccount(){
		Account account=new Account();
 
		account.setAccountId(new Random().nextInt(start)+100000000L);
		account.setEmail("xxx"+System.currentTimeMillis()+"@sogou.com"); 
		account.setPassword(""+System.currentTimeMillis());
		account.setRegistDate(new Date());
		 
		return account;
		
	}
	 
}
