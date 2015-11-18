package com.sogou.bizdev.compass.sample.hibernate.shard.service;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;



/**Plan的分库HibernateService样例
 * @author gly
 *
 */
public interface ShardHibernatePlanService {
	
	/**获取一个计划
	 * @param accountId
	 * @param planId
	 * @return
	 */
	public Plan getPlanById(@RouteKey Long accountId, Long planId); 
	
	/**创建一个计划
	 * @param accountId
	 * @param plan
	 */
	public void createPlan(@RouteKey Long accountId, Plan plan);
}
