package com.sogou.bizdev.compass.sample.hibernate.masterslave.service;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;

/**
 * 读写嵌套调用情况
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
public interface HibernateAccountMSService {
	/**
	 * 主-》从, ok
	 * @param accountId
	 */
	public void createSlaveInMaster(AccountForTest accountForTest);

	/**
	 * 主-》主，ok
	 * @param accountId
	 */
	public void createMasterInMaster(AccountForTest accountForTest);
	
	/**
	 * 从-》从， ok
	 * @param accountId
	 * @return
	 */
	public AccountForTest findSlaveInSlave(Long accountId);
	
	/**
	 * 从-》主， error
	 * @param accountId
	 * @return
	 */
	public AccountForTest findWriteInRead(AccountForTest accountForTest);
	
}
