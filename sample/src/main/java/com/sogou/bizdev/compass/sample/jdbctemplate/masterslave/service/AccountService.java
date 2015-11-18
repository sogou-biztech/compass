package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.Account;



public interface AccountService {
	
	public Account getAccountById(Long accountId);
	
}
