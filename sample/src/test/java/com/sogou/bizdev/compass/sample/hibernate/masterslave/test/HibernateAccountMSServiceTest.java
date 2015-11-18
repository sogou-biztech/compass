package com.sogou.bizdev.compass.sample.hibernate.masterslave.test;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountMSService;

/**
 * 读写嵌套调用情况
 * 
 * @author xr
 * @since 1.0.0
 */
@ContextConfiguration(locations = { "classpath*:/conf/hibernate/test-masterslave-*.xml","classpath*:/datasource/masterslave/test-masterslave-*.xml" })
public class HibernateAccountMSServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void testCreateSlaveInMaster(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(99999998L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountMSService.createSlaveInMaster(accountForTest);

		System.out.println(accountForTest);
	}

	@Test
	public void testCreateMasterInMaster(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(99999998L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		accountMSService.createMasterInMaster(accountForTest);

		System.out.println(accountForTest);
		
	}
	
	@Test
	public void testFindSlaveInSlave(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = accountMSService.findSlaveInSlave(99999998L);

		System.out.println(accountForTest);
	}
	
	@Test
	public void testFindWriteInRead(){
		HibernateAccountMSService accountMSService = (HibernateAccountMSService)applicationContext.getBean("hibernateAccountMSService");
		
		Account accountForTest = new Account();
		accountForTest.setAccountId(99999997L);
		accountForTest.setEmail("xxx@sogou.com");
		accountForTest.setPassword("xxxxxxx");
		accountForTest.setRegistDate(new Date());
		try{
			accountMSService.findWriteInRead(accountForTest);
		}catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(accountForTest);
	}
	
}
