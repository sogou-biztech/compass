package com.sogou.bizdev.compass.sample.mybatis.masterslave.service;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Account;



public interface MybatisAccountService {
	/**查询账户
	 * @param AccountId
	 * @return
	 */
	public Account getAccountByAccountId(Long accountId);
	
	/**查询多个账户
	 * @param AccountId
	 * @return
	 */
	public List<Account> getAccountsByAccountIds(List<Long> accountIds);
	
	/**新增账户
	 * @param account
	 * @return
	 */
	public int createAccount(Account account);
	
	/**更新账户
	 * @param account
	 * @return
	 */
	public int updateAccount(Account account);
	
	/**删除账户
	 * @param accountId
	 * @return
	 */
	public int deleteAccount(Long accountId);
	 
	
}
