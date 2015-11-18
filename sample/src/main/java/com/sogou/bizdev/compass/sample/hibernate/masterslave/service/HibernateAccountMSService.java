package com.sogou.bizdev.compass.sample.hibernate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.Account;

/**
 * 读写嵌套调用情况
 * 
 * @author xr
 * @since 1.0.0
 */
public interface HibernateAccountMSService {
	/**
	 * 主-》从, ok
	 * @param accountId
	 */
	public void createSlaveInMaster(Account accountForTest);

	/**
	 * 主-》主，ok
	 * @param accountId
	 */
	public void createMasterInMaster(Account accountForTest);
	
	/**
	 * 从-》从， ok
	 * @param accountId
	 * @return
	 */
	public Account findSlaveInSlave(Long accountId);
	
	/**
	 * 从-》主， error
	 * @param accountId
	 * @return
	 */
	public Account findWriteInRead(Account accountForTest);
	
}
