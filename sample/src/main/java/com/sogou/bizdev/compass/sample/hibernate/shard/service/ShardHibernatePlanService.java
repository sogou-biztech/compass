package com.sogou.bizdev.compass.sample.hibernate.shard.service;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;



public interface ShardHibernatePlanService {
	
	public Plan getPlanById(@RouteKey Long accountId, Long planId); 
	
	public void createPlan(@RouteKey Long accountId, Plan plan);
}
