package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.Account;



/**Account的jdbcService样例
 * @author gly
 *
 */
public interface AccountService {
	
	/**获取一个账户
	 * @param accountId
	 * @return
	 */
	public Account getAccountById(Long accountId);
	
}
