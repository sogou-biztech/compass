package com.sogou.bizdev.compass.sample.hibernate.masterslave.test;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;

@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml" })
public class HibernateAccountServiceTest extends AbstractJUnit4SpringContextTests {
    
	@Test
	public void testGetAccountById() {
		HibernateAccountService accountService = (HibernateAccountService)applicationContext.getBean("hibernateAccountService");
		Account accountForTest = accountService.getAccountById(375832L);
		Assert.assertNotNull(accountForTest);
		Assert.assertEquals(accountForTest.getEmail(), "xxx@sogou.com");
		System.out.println(accountForTest);
	}
	
	@Test
	public void testInsert() {
		HibernateAccountService accountService = (HibernateAccountService)applicationContext.getBean("hibernateAccountService");
		Account accountForTest = new Account();
		accountForTest.setAccountId(99999999L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountService.createAccount(accountForTest);
		
		Account accountForTest1 = accountService.getAccountById(99999999L);
		accountForTest1 = accountService.getAccountById(99999999L);
		accountForTest1 = accountService.getAccountById(99999999L);
		accountForTest1 = accountService.getAccountById(99999999L);
		System.out.println(accountForTest1);
	}

}
