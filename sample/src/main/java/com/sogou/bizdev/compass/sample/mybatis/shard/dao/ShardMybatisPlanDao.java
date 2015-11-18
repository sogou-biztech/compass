package com.sogou.bizdev.compass.sample.mybatis.shard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.common.po.PlanExample;

/**Plan的分库mybatisDao样例
 * @author gly
 *
 */
public interface ShardMybatisPlanDao {
    
    /**创建一个计划
     * @param record
     * @return
     */
    public int createPlan(Plan record);
 
    /**根据planIds查询Plan
     * @param example
     * @return
     */
    public List<Plan> queryPlansByPlanIds(PlanExample example);
   
    /**获取一个Plan
     * @param planId
     * @return
     */
    public Plan getPlanByPlanId(Long planId);
 
    /**更新Plan
     * @param plan
     * @return
     */
    public int updatePlan(Plan plan);
    
    /**删除Plan
     * @param planId
     * @return
     */
    public int deletePlan(Long planId);
}