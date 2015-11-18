package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.service.impl;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.dao.AccountDao;
import com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.service.AccountService;

public class AccountServiceImpl implements AccountService {

	private AccountDao accountDao;

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public Account getAccountById(Long accountId) {
		return getAccountDao().getAccountById(accountId);
	}

}
