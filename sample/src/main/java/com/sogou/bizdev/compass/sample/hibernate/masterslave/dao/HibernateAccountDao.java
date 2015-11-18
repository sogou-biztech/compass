package com.sogou.bizdev.compass.sample.hibernate.masterslave.dao;

import com.sogou.bizdev.compass.sample.common.po.Account;


/**Account的HibernateDao样例
 * @author gly
 *
 */
public interface HibernateAccountDao {
	/**获取一个账户
	 * @param accountId
	 * @return
	 */
	public Account getAccountById(Long accountId);
	
	/**插入一个账户
	 * @param account
	 */
	public void createAccount(Account account);
}
