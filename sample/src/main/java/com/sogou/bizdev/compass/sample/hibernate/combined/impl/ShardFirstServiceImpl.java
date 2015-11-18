package com.sogou.bizdev.compass.sample.hibernate.combined.impl;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.combined.ShardFirstService;
import com.sogou.bizdev.compass.sample.hibernate.masterslave.service.HibernateAccountService;
import com.sogou.bizdev.compass.sample.hibernate.shard.service.ShardHibernatePlanService;

public class ShardFirstServiceImpl implements ShardFirstService{

	private HibernateAccountService hibernateAccountService;
	private ShardHibernatePlanService shardHibernatePlanService;
	
	public HibernateAccountService getHibernateAccountService() {
		return hibernateAccountService;
	}

	public void setHibernateAccountService(
			HibernateAccountService hibernateAccountService) {
		this.hibernateAccountService = hibernateAccountService;
	}

	public ShardHibernatePlanService getShardHibernatePlanService() {
		return shardHibernatePlanService;
	}

	public void setShardHibernatePlanService(
			ShardHibernatePlanService shardHibernatePlanService) {
		this.shardHibernatePlanService = shardHibernatePlanService;
	}

	@Override
	public Plan getMSAndShardPlan(Long accountId, Long planId) {
		Account accountForTest = hibernateAccountService.getAccountById(accountId);
		return shardHibernatePlanService.getPlanById(accountForTest.getAccountId(), planId);
	}




}
