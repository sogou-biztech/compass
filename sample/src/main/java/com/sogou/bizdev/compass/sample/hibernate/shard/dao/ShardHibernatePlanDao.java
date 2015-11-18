package com.sogou.bizdev.compass.sample.hibernate.shard.dao;

import com.sogou.bizdev.compass.sample.common.po.Plan;


/**Plan的分库HibernateDao样例
 * @author gly
 *
 */
public interface ShardHibernatePlanDao {
	
	/**获取一个计划
	 * @param accountId
	 * @param planId
	 * @return
	 */
	public Plan getPlanById(Long accountId, Long planId);
	
	/**创建一个计划
	 * @param accountId
	 * @param plan
	 */
	public void createPlan(Long accountId, Plan plan);

}
