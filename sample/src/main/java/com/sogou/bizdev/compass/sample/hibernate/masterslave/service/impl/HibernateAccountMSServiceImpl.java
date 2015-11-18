package com.sogou.bizdev.compass.sample.hibernate.masterslave.service.impl;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;
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
	public void createSlaveInMaster(AccountForTest accountForTest) {
		hibernateAccountService.getAccountById(accountForTest.getAccountId());
	}

	@Override
	public void createMasterInMaster(AccountForTest accountForTest) {
		hibernateAccountService.createAccountForTest(accountForTest);
	}

	@Override
	public AccountForTest findSlaveInSlave(Long accountId) {
		return hibernateAccountService.getAccountById(accountId);
	}

	@Override
	public AccountForTest findWriteInRead(AccountForTest accountForTest) {
		hibernateAccountService.createAccountForTest(accountForTest);
		return hibernateAccountService.getAccountById(accountForTest.getAccountId());
	}

}
