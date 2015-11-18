package com.sogou.bizdev.compass.sample.jdbctemplate.shard.service.impl;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao.ShardPlanDao;
import com.sogou.bizdev.compass.sample.jdbctemplate.shard.service.ShardPlanService;

public class ShardPlanServiceImpl implements ShardPlanService {

	private ShardPlanDao planDao;

	public ShardPlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(ShardPlanDao planDao) {
		this.planDao = planDao;
	}

	@Override
	public List<Plan> getPlansById(Long accountId) {
		return getPlanDao().getPlansById(accountId);
	}

	public Plan updatePlanById(Long accountId, Plan p){
		return getPlanDao().updatePlanById(accountId, p);
	}
}
