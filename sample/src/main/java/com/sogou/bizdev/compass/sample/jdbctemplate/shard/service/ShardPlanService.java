package com.sogou.bizdev.compass.sample.jdbctemplate.shard.service;

import java.util.List;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;



/**Plan的分库jdbcService样例
 * @author gly
 *
 */
public interface ShardPlanService {
	
	/**根据accountId获取Plan
	 * @param accountId
	 * @return
	 */
	public Plan createPlans(@RouteKey Long accountId,Plan plan);
	
	/**根据accountId获取Plan
	 * @param accountId
	 * @return
	 */
	public List<Plan> getPlansByAccountId(@RouteKey Long accountId);
	
	/**更新Plan
	 * @param accountId
	 * @param p
	 * @return
	 */
	public Plan updatePlan(@RouteKey Long accountId, Plan p);
	
}
