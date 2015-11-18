package com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao;

import java.util.List;

import com.sogou.bizdev.compass.sample.common.po.Plan;


/**Plan的分库jdbc样例
 * @author gly
 *
 */
public interface ShardPlanDao {
	/**获取一个账户下所有的Plan
	 * @param accountId
	 * @return
	 */
	public Plan createPlan(Long accountId,Plan plan);
	  
	/**获取一个账户下所有的Plan
	 * @param accountId
	 * @return
	 */
	public List<Plan> getPlansByAccountId(Long accountId);
	
	/**获取一个Plan
	 * @param accountId
	 * @param planId
	 * @return
	 */
	public Plan getPlanByPlanId(Long accountId,Long planId);
	
	/**更新一个Plan
	 * @param accountId
	 * @param p
	 * @return
	 */
	public Plan updatePlan(Long accountId, Plan p);
}