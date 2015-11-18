package com.sogou.bizdev.compass.sample.hibernate.masterslave.service.impl;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.HibernateAccountForTestDao;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;

public class HibernateAccountServiceImpl implements HibernateAccountService {

	private HibernateAccountForTestDao hibernateAccountForTestDao;

	public HibernateAccountForTestDao getHibernateAccountForTestDao() {
		return hibernateAccountForTestDao;
	}

	public void setHibernateAccountForTestDao(
			HibernateAccountForTestDao hibernateAccountForTestDao) {
		this.hibernateAccountForTestDao = hibernateAccountForTestDao;
	}

	@Override
	public AccountForTest getAccountById(Long accountId) {
		return hibernateAccountForTestDao.getAccountById(accountId);
	}

	@Override
	public void createAccountForTest(AccountForTest accountForTest) {
		hibernateAccountForTestDao.insert(accountForTest);
	}
	


}
