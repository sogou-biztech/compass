package com.sogou.bizdev.compass.sample.hibernate.masterslave.dao;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;


public interface HibernateAccountForTestDao {
	public AccountForTest getAccountById(Long accountId);
	
	public void insert(AccountForTest accountForTest);
}
