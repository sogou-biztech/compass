package com.sogou.bizdev.compass.sample.hibernate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.Account;



/**Account的HibernateService样例
 * @author gly
 *
 */
public interface HibernateAccountService {
	
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
