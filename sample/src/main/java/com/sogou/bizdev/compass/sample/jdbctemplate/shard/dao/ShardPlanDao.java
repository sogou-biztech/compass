package com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Plan;


public interface ShardPlanDao {
	  
	public List<Plan> getPlansById(Long accountId);
	
	public Plan updatePlanById(Long accountId, Plan p);
}