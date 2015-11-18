package com.sogou.bizdev.compass.sample.jdbctemplate.shard.service;

import java.util.List;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;



public interface ShardPlanService {
	
	public List<Plan> getPlansById(@RouteKey Long accountId);
	
	public Plan updatePlanById(@RouteKey Long accountId, Plan p);
	
}
