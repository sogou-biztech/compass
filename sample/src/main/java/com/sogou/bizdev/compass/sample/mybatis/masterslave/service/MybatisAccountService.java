package com.sogou.bizdev.compass.sample.mybatis.masterslave.service;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;



public interface MybatisAccountService {
	/**查询账户
	 * @param AccountId
	 * @return
	 */
	public AccountForTest queryAccountByAccountId(Long accountId);
	
	/**查询多个账户
	 * @param AccountId
	 * @return
	 */
	public List<AccountForTest> queryAccountsByAccountIds(List<Long> AccountIds);
	
	/**新增账户
	 * @param AccountForTest
	 * @return
	 */
	public int insert(AccountForTest AccountForTest);
	
	/**更新账户
	 * @param AccountForTest
	 * @return
	 */
	public int update(AccountForTest AccountForTest);
	
	/**删除账户
	 * @param AccountId
	 * @return
	 */
	public int delete(Long AccountId);
	 
	
}
