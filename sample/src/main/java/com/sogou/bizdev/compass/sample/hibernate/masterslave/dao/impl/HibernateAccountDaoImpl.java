package com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.HibernateAccountDao;

public class HibernateAccountDaoImpl implements HibernateAccountDao {

	private HibernateTemplate masterSlaveHibernateTemplate;
	
	public HibernateTemplate getMasterSlaveHibernateTemplate() {
		return masterSlaveHibernateTemplate;
	}

	public void setMasterSlaveHibernateTemplate(
			HibernateTemplate masterSlaveHibernateTemplate) {
		this.masterSlaveHibernateTemplate = masterSlaveHibernateTemplate;
	}

	@Override
	public Account getAccountById(Long accountId) {
		List list = masterSlaveHibernateTemplate.find("from Account where accountId=" + accountId);
		return (list == null || list.size() == 0) ? null : (Account) list.get(0);
	}

	@Override
	public void createAccount(Account accountForTest) {
		masterSlaveHibernateTemplate.save(accountForTest);		
	}
    

}
