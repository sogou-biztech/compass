package com.sogou.bizdev.compass.sample.mybatis.shard.service.impl;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.common.po.PlanExample;
import com.sogou.bizdev.compass.sample.mybatis.shard.dao.ShardMybatisPlanDao;
import com.sogou.bizdev.compass.sample.mybatis.shard.service.ShardMybatisPlanService;

public class ShardMybatisPlanServiceImpl implements ShardMybatisPlanService {

	private ShardMybatisPlanDao planDao;

	public ShardMybatisPlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(ShardMybatisPlanDao planDao) {
		this.planDao = planDao;
	}

	@Override
	public Plan queryPlanByPlanId(Long accountId,Long planId) {
 		return planDao.getPlanByPlanId(planId);
	}

	@Override
	public List<Plan> queryPlansByPlanIds(Long accountId,List<Long> planIds) {
		PlanExample planExample=new PlanExample();
		planExample.createCriteria().andPlanIdIn(planIds);
		planExample.createCriteria().andAccountIdEqualTo(accountId); 
		return planDao.queryPlansByPlanIds(planExample);
	}

	@Override
	public int createPlan(Long accountId,Plan plan) {
		return planDao.createPlan(plan);
	}

	@Override
	public int updatePlan(Long accountId,Plan plan) {
		return planDao.updatePlan(plan);
	}

	@Override
	public int deletePlan(Long accountId,Long planId) {
		return planDao.deletePlan(planId);
	}

}
