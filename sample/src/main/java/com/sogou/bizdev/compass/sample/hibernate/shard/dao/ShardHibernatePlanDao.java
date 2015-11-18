package com.sogou.bizdev.compass.sample.hibernate.shard.dao;

import com.sogou.bizdev.compass.sample.common.po.Plan;


public interface ShardHibernatePlanDao {
	public Plan findById(Long accountId, Long planId);
	public void create(Long accountId, Plan plan);

}
