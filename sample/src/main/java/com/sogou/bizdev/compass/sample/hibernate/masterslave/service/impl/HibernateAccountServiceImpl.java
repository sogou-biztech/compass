package com.sogou.bizdev.compass.sample.hibernate.masterslave.service.impl;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.HibernateAccountDao;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;

public class HibernateAccountServiceImpl implements HibernateAccountService {

	private HibernateAccountDao hibernateAccountDao;

	public HibernateAccountDao getHibernateAccountDao() {
		return hibernateAccountDao;
	}

	public void setHibernateAccountDao(
			HibernateAccountDao hibernateAccountDao) {
		this.hibernateAccountDao = hibernateAccountDao;
	}

	@Override
	public Account getAccountById(Long accountId) {
		return hibernateAccountDao.getAccountById(accountId);
	}

	@Override
	public void createAccount(Account account) {
		hibernateAccountDao.createAccount(account);
	}
}
