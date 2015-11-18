package com.sogou.bizdev.compass.sample.hibernate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;



public interface HibernateAccountService {
	
	public AccountForTest getAccountById(Long accountId);
	
	public void createAccountForTest(AccountForTest accountForTest);
	
}
