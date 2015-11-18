package com.sogou.bizdev.compass.sample.aggregation;

import java.util.List;

import com.sogou.bizdev.compass.aggregation.exception.SqlExecutionFailureException;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.common.po.PlanCount;

public interface ShardJdbcTemplatePlanService {
	
	/**
	 * 根据cpcplanid查询对应的Plan实体
	 * 
	 * 此方法用于演示扫描所有分表的查询
	 * @param planId
	 * @return
	 */
	public Plan queryPlanByPlanId(Long planId);
	
	/**
	 * 列出所有的cpcplan
	 * 
	 * 此方法用于演示数据量超过ShardJdbcTemplate.maxRows后抛出SqlExecutionFailureException
	 * @return
	 */
	public List<Plan> listAllPlan() throws SqlExecutionFailureException;
	
	/**
	 * 分页查询cpcplan
	 * 
	 * 此方法用于演示聚合层的limit功能
	 * @param pageCount 起始值为0
	 * @param pageSize
	 * @return
	 */
	public List<Plan> listPlan(int pageCount, int pageSize);
	
	/**
	 * 根据account_id查询cpcplan
	 * 
	 * 此方法用于演示聚合层的指定分表依据批量查询功能
	 * @param accountIds
	 * @return
	 */
	public List<Plan> queryPlansByAccountIds(List<Long> accountIds);
	
	/**
	 * 根据startTime, endTime, account_id查询cpcplan
	 * 
	 * 此方法用于演示聚合层的指定分表依据批量查询时如果构造sql的参数
	 * @param accountIds
	 * @return
	 */
	public List<Plan> queryPlansByAccountIdsAndTime(List<Long> accountIds, String startTime, String endTime);
	
	/**
	 * 传入一组accountId，查询每个account_id下的cpcplan数目
	 * 
	 * 此方法用于演示聚合层的聚合功能
	 * 
	 * @param accountIds
	 * @return
	 */
	public List<PlanCount> countPlanOfAccounts(List<Long> accountIds);
	
	/**
	 * 查询cpcplan中每个account_id下的cpcplan数目
	 * 
	 * 此方法用于演示聚合层的聚合功能
	 * @return
	 */
	public List<PlanCount> countAllPlanOfAccounts();
	
	/**
	 * 根据cpcplanid更新cpcplan
	 * 
	 * 此方法用于演示扫描所有分表的更新操作
	 * @param plan
	 */
	public void updatePlan(Plan plan);
	
	/**
	 * 更新一组cpcplan
	 * 
	 * 此方法用于演示批量更新操作
	 * @param planList
	 */
	public void updatePlans(List<Plan> planList);

}
