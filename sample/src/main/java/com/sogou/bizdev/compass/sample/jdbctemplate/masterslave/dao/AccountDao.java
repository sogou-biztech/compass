package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.dao;

import com.sogou.bizdev.compass.sample.common.po.Account;


/**Account的jdbc样例
 * @author gly
 *
 */
public interface AccountDao {
	 /**获取一个账户
	 * @param accountId
	 * @return
	 */
	public Account getAccountById(Long accountId);
}