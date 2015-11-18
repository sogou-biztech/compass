package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.dao;

import com.sogou.bizdev.compass.sample.common.po.Account;


public interface AccountDao {
	 public Account getAccountById(Long accountId);
}