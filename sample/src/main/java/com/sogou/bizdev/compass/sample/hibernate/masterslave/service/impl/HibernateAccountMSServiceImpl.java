package com.sogou.bizdev.compass.sample.hibernate.masterslave.service.impl;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountMSService;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;

public class HibernateAccountMSServiceImpl implements HibernateAccountMSService {

	private HibernateAccountService hibernateAccountService;
	
	public HibernateAccountService getHibernateAccountService() {
		return hibernateAccountService;
	}

	public void setHibernateAccountService(
			HibernateAccountService hibernateAccountService) {
		this.hibernateAccountService = hibernateAccountService;
	}

	@Override
	public void createSlaveInMaster(Account accountForTest) {
		hibernateAccountService.getAccountById(accountForTest.getAccountId());
	}

	@Override
	public void createMasterInMaster(Account accountForTest) {
		hibernateAccountService.createAccount(accountForTest);
	}

	@Override
	public Account findSlaveInSlave(Long accountId) {
		return hibernateAccountService.getAccountById(accountId);
	}

	@Override
	public Account findWriteInRead(Account accountForTest) {
		hibernateAccountService.createAccount(accountForTest);
		return hibernateAccountService.getAccountById(accountForTest.getAccountId());
	}

}
