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
	public Plan createPlans(Long accountId, Plan plan) {
 		return getPlanDao().createPlan(accountId,plan);
	}

	@Override
	public List<Plan> getPlansByAccountId(Long accountId) {
		return getPlanDao().getPlansByAccountId(accountId);
	}

	public Plan updatePlan(Long accountId, Plan p){
		return getPlanDao().updatePlan(accountId, p);
	}

	
}
