package com.sogou.bizdev.compass.sample.hibernate.masterslave.test;

import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.core.exception.MasterSlaveConfigInconsistencyException;
import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountMSService;

/**
 * 读写嵌套调用情况
 * 
 * @author xr
 * @since 1.0.0
 */
@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml","classpath*:/conf/hibernate/test-shard-*.xml","classpath*:/datasource/shard/test-shard-*.xml"  })
public class HibernateAccountMSServiceTest extends AbstractJUnit4SpringContextTests{
	private final static Integer start=65535;
	private static Long newAccountId=null;
	
	@Test
	public void testCreateSlaveInMaster(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(new Random().nextInt(start)+100000000L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountMSService.createSlaveInMaster(accountForTest);
		Assert.assertTrue(accountForTest!=null); 
	}

	@Test
	public void testCreateMasterInMaster(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(new Random().nextInt(start)+100000000L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountMSService.createMasterInMaster(accountForTest);
		Assert.assertTrue(accountForTest!=null); 
		newAccountId=accountForTest.getAccountId();
	}
	
	@Test
	public void testFindSlaveInSlave(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = accountMSService.findSlaveInSlave(newAccountId);
		Assert.assertTrue(accountForTest!=null); 
	}
	
	@Test(expected = MasterSlaveConfigInconsistencyException.class)
	public void testFindWriteInRead(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(new Random().nextInt(start)+100000000L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountMSService.findWriteInRead(accountForTest);
	}
	
}
