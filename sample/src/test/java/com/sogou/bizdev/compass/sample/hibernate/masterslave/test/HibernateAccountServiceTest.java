package com.sogou.bizdev.compass.sample.hibernate.masterslave.test;

import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;

@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml","classpath*:/conf/hibernate/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml"  })
public class HibernateAccountServiceTest extends AbstractJUnit4SpringContextTests {
	private static Long newAccountId=null;
	private final static Integer start=65535;
	
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
	public void testGetAccountById() {
		HibernateAccountService accountService = (HibernateAccountService)applicationContext.getBean("hibernateAccountService");
		Account account = accountService.getAccountById(newAccountId);
		Assert.assertNotNull(account);
		Assert.assertEquals(account.getEmail(), "xxx@sogou.com");
	}
	
	public Account createAccount(){
		Account account=new Account();
		account.setAccountId(new Random().nextInt(start)+100000000L);

		account.setEmail("xxx@sogou.com");
		account.setPassword("xxxxxx");
		account.setRegistDate(new Date()); 
		return account;
		
	}

}
