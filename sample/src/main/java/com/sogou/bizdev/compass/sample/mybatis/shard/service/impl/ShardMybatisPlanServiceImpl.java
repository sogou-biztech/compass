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
 		return planDao.selectByPrimaryKey(planId);
	}

	@Override
	public List<Plan> queryPlansByPlanIds(Long accountId,List<Long> planId) {
		PlanExample planExample=new PlanExample();
		planExample.createCriteria().andCpcplanidIn(planId);
		planExample.createCriteria().andAccountidEqualTo(accountId); 
		return planDao.selectByExample(planExample);
	}

	@Override
	public int insert(Long accountId,Plan plan) {
		return planDao.insert(plan);
	}

	@Override
	public int update(Long accountId,Plan plan) {
		return planDao.updateByPrimaryKey(plan);
	}

	@Override
	public int delete(Long accountId,Long planId) {
		return planDao.deleteByPrimaryKey(planId);
	}

}
