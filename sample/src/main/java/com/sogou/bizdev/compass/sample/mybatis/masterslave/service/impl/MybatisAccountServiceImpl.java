package com.sogou.bizdev.compass.sample.mybatis.masterslave.service.impl;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;
import com.sogou.bizdev.compass.sample.common.po.AccountForTestExample;
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
	public AccountForTest queryAccountByAccountId(Long accountId) {
 		return accountDao.selectByPrimaryKey(accountId);
	}

	@Override
	public List<AccountForTest> queryAccountsByAccountIds(List<Long> AccountIds) {
		AccountForTestExample accountForTestExample=new AccountForTestExample();
		accountForTestExample.createCriteria().andAccountIdIn(AccountIds);
 		return accountDao.selectByExample(accountForTestExample);
	}

	@Override
	public int insert(AccountForTest AccountForTest) {
		return accountDao.insert(AccountForTest);
	}

	@Override
	public int update(AccountForTest AccountForTest) {
		return accountDao.updateByPrimaryKey(AccountForTest);
	}

	@Override
	public int delete(Long AccountId) {
		return accountDao.deleteByPrimaryKey(AccountId);
	}

	 

}
