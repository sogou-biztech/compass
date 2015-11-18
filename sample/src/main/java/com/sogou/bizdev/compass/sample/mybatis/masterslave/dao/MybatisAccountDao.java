package com.sogou.bizdev.compass.sample.mybatis.masterslave.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.common.po.AccountExample;

/**Account的mybatisDao样例
 * @author gly
 *
 */
public interface MybatisAccountDao {
 
    /**插入一个账户
     * @param record
     * @return
     */
    public int createAccount(Account record);
 
    /**根据ids获取账户
     * @param example
     * @return
     */
    public List<Account> getAccountsByAccountIds(AccountExample example);
 
    /**根据id获取账户
     * @param accountId
     * @return
     */
    public Account getAccountByAccountId(Long accountId);

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