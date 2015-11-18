package com.sogou.bizdev.compass.sample.mybatis.masterslave.service.impl;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.common.po.AccountExample;
import com.sogou.bizdev.compass.sample.mybatis.masterslave.dao.MybatisAccountDao;
import com.sogou.bizdev.compass.sample.mybatis.masterslave.service.MybatisAccountService;

public class MybatisAccountServiceImpl implements MybatisAccountService {

	private MybatisAccountDao accountDao;

	public MybatisAccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(MybatisAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public Account getAccountByAccountId(Long accountId) {
 		return accountDao.getAccountByAccountId(accountId);
	}

	@Override
	public List<Account> getAccountsByAccountIds(List<Long> AccountIds) {
		AccountExample accountForTestExample=new AccountExample();
		accountForTestExample.createCriteria().andAccountIdIn(AccountIds);
 		return accountDao.getAccountsByAccountIds(accountForTestExample);
	}

	@Override
	public int createAccount(Account AccountForTest) {
		return accountDao.createAccount(AccountForTest);
	}

	@Override
	public int updateAccount(Account AccountForTest) {
		return accountDao.updateAccount(AccountForTest);
	}

	@Override
	public int deleteAccount(Long AccountId) {
		return accountDao.deleteAccount(AccountId);
	}

	 

}
