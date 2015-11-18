package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.service.AccountService;

@ContextConfiguration(locations = { "classpath*:/conf/jdbctemplate/test-masterslave-*.xml","classpath*:test-masterslave-*.xml" })
public class AccountServiceTest extends AbstractJUnit4SpringContextTests {

    private static Logger logger = Logger.getLogger(AccountServiceTest.class);  
    
	@Test
	public void testGetAccount() {
		
		AccountService accountService = (AccountService)applicationContext.getBean("accountService", AccountService.class);
		Account a = accountService.getAccountById(274280L);
		System.out.println(a);
	}
	
	 

}
