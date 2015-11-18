package com.sogou.bizdev.compass.sample.mybatis.shard.service;

import java.util.List;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;



public interface ShardMybatisPlanService {
	/**查询计划
	 * @param planId
	 * @return
	 */
	public Plan queryPlanByPlanId(@RouteKey Long accountId,Long planId);
	
	/**查询多个计划
	 * @param planId
	 * @return
	 */
	public List<Plan> queryPlansByPlanIds(@RouteKey Long accountId,List<Long> planIds);
	
	/**新增计划
	 * @param plan
	 * @return
	 */
	public int createPlan(@RouteKey Long accountId,Plan plan);
	
	/**更新计划
	 * @param plan
	 * @return
	 */
	public int updatePlan(@RouteKey Long accountId,Plan plan);
	
	/**删除计划
	 * @param planId
	 * @return
	 */
	public int deletePlan(@RouteKey Long accountId,Long planId);
}
