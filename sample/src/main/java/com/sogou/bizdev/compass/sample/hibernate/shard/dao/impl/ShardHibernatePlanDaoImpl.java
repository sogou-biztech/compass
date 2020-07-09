package com.sogou.bizdev.compass.sample.hibernate.shard.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.hibernate.shard.dao.ShardHibernatePlanDao;

public class ShardHibernatePlanDaoImpl implements ShardHibernatePlanDao {

	private HibernateTemplate shardHibernateTemplate;
	
	public HibernateTemplate getShardHibernateTemplate() {
		return shardHibernateTemplate;
	}

	public void setShardHibernateTemplate(HibernateTemplate shardHibernateTemplate) {
		this.shardHibernateTemplate = shardHibernateTemplate;
	}

	@Override
	public Plan getPlanById(Long accountId, Long planId) {
		List list = shardHibernateTemplate.find("from Plan where accountId=" + accountId + " and planId=" + planId);
		return (list == null || list.size() == 0) ? null : (Plan)list.get(0);
	}

	@Override
	public void createPlan(Long accountId, Plan plan) {
		shardHibernateTemplate.save(plan);
	}
        
}
