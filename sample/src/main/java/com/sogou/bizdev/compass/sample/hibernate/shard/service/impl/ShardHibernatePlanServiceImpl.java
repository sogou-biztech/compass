package com.sogou.bizdev.compass.sample.hibernate.shard.service.impl;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.shard.dao.ShardHibernatePlanDao;
import com.sogou.bizdev.compass.sample.hibernate.shard.service.ShardHibernatePlanService;

public class ShardHibernatePlanServiceImpl implements ShardHibernatePlanService {

	private ShardHibernatePlanDao shardHibernatePlanDao;

	public ShardHibernatePlanDao getShardHibernatePlanDao() {
		return shardHibernatePlanDao;
	}

	public void setShardHibernatePlanDao(ShardHibernatePlanDao shardHibernatePlanDao) {
		this.shardHibernatePlanDao = shardHibernatePlanDao;
	}


	@Override
	public Plan getPlanById(Long accountId, Long planId) {
		return shardHibernatePlanDao.findById(accountId, planId);
	}

	@Override
	public void createPlan(Long accountId, Plan plan) {
		shardHibernatePlanDao.create(accountId, plan);
	}

}
