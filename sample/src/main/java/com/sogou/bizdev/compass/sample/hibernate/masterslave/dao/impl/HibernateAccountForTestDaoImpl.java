package com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.sogou.bizdev.compass.sample.common.po.AccountForTest;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.dao.HibernateAccountForTestDao;

public class HibernateAccountForTestDaoImpl implements HibernateAccountForTestDao {

	private HibernateTemplate masterSlaveHibernateTemplate;
	
	public HibernateTemplate getMasterSlaveHibernateTemplate() {
		return masterSlaveHibernateTemplate;
	}

	public void setMasterSlaveHibernateTemplate(
			HibernateTemplate masterSlaveHibernateTemplate) {
		this.masterSlaveHibernateTemplate = masterSlaveHibernateTemplate;
	}

	@Override
	public AccountForTest getAccountById(Long accountId) {
		List list = masterSlaveHibernateTemplate.find("from AccountForTest where accountId=" + accountId);
		return (list == null || list.size() == 0) ? null : (AccountForTest) list.get(0);
	}

	@Override
	public void insert(AccountForTest accountForTest) {
		masterSlaveHibernateTemplate.save(accountForTest);		
	}
    

}
