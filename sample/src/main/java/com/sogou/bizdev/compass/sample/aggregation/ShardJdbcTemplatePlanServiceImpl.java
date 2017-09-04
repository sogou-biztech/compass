package com.sogou.bizdev.compass.sample.aggregation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregatedRowMapper;
import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;
import com.sogou.bizdev.compass.aggregation.template.ShardJdbcTemplate;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.common.po.PlanCount;
import com.sogou.bizdev.compass.sample.common.po.PlanMapper;


public class ShardJdbcTemplatePlanServiceImpl implements ShardJdbcTemplatePlanService {
	
	private ShardJdbcTemplate shardJdbcTemplate;
	
	@Override
	public Plan queryPlanByPlanId(Long planId) {
		String sql = "select * from plan where planid=?";
		Object[] args = new Object[] { planId };
		PlanMapper mapper = new PlanMapper();
		
		List<Plan> result = shardJdbcTemplate.query(sql, args, mapper);
		
		if (result != null && result.size() > 0) {
			return result.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public List<Plan> listAllPlan() {
		String sql = "select * from plan";
		return shardJdbcTemplate.query(sql, null, new PlanMapper());
	}
	
	@Override
	public List<Plan> listPlan(int pageCount, int pageSize) {
		if (pageCount < 0 || pageSize < 0) {
			throw new IllegalArgumentException();
		}

		int originMaxRows = shardJdbcTemplate.getMaxRows();
		try {
			// pageCount起始值为0
			//分页
			int offset = pageCount * pageSize;
			int rows = pageSize;
			
			String sql = "select * from plan";
			AggregationDescriptor descriptor = new AggregationDescriptor()
				.orderBy("planid", true)
				.limit(offset, rows);
			
			shardJdbcTemplate.setMaxRows(1000000);
			return shardJdbcTemplate.query(sql, null, this.getAggregatedPlanMapper(), descriptor);
		} finally {
			shardJdbcTemplate.setMaxRows(originMaxRows);
		}
	}

	@Override
	public List<Plan> queryPlansByAccountIds(List<Long> accountIds) {
		String sql = "select * from plan where accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")";
		Object[] args = new Object[] { accountIds };
		PlanMapper mapper = new PlanMapper();

		return shardJdbcTemplate.query(sql, args, mapper);
	}

	@Override
	public List<Plan> queryPlansByAccountIdsAndTime(List<Long> accountIds, String startTime, String endTime) {
		String sql = "select * from plan where"
				+ " createdate > ?"
				+ " and accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")"
				+ " and createdate < ?";
		Object[] args = new Object[] { startTime, accountIds, endTime };
		PlanMapper mapper = new PlanMapper();

		return shardJdbcTemplate.query(sql, args, mapper);
	}

	@Override
	public List<PlanCount> countPlanOfAccounts(List<Long> accountIds) {
		String sql = "select planid, accountid from plan where accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")";
		Object[] args = new Object[] { accountIds };
		AggregationDescriptor descriptor = new AggregationDescriptor()
			.count("planid", "planidCount")
			.groupBy("accountid")
			.orderBy("accountid", true);
		AggregatedRowMapper<PlanCount> mapper = this.getPlanCountMapper();

		return shardJdbcTemplate.query(sql, args, mapper, descriptor);
	}
	
	private AggregatedRowMapper<PlanCount> getPlanCountMapper() { 
		return new AggregatedRowMapper<PlanCount>() {
			@Override
			public PlanCount mapRow(Map<String, Object> aggregatedRow) {
                PlanCount planCount = new PlanCount();
                
                planCount.setAccountId((Long) aggregatedRow.get("accountid"));
                planCount.setPlanCount((Integer) aggregatedRow.get("planidCount"));
                
                return planCount;
			}
		};
	}
	
	public AggregatedRowMapper<Plan> getAggregatedPlanMapper() {
		return new AggregatedRowMapper<Plan>() {

			@Override
			public Plan mapRow(Map<String, Object> row) {
				Plan plan = new Plan();
				
				plan.setPlanId((Long) row.get("planid"));
				plan.setName((String) row.get("name"));
 				plan.setAccountId((Long) row.get("accountid"));
				plan.setCreateDate((Date) row.get("createdate"));
				return plan;
			};
		};
	}
	
	@Override
	public List<PlanCount> countAllPlanOfAccounts() {
		int originMaxRows = shardJdbcTemplate.getMaxRows();
		try {
			String sql = "select planid, accountid from plan";
			AggregationDescriptor descriptor = new AggregationDescriptor()
				.count("planid", "planidCount")
				.groupBy("accountid")
				.orderBy("accountid", true);
			AggregatedRowMapper<PlanCount> mapper = this.getPlanCountMapper();
			
			shardJdbcTemplate.setMaxRows(10000000);
			return shardJdbcTemplate.query(sql, null, mapper, descriptor);
		} finally {
			shardJdbcTemplate.setMaxRows(originMaxRows);
		}
	}

	public ShardJdbcTemplate getShardJdbcTemplate() {
		return shardJdbcTemplate;
	}

	public void setShardJdbcTemplate(ShardJdbcTemplate shardJdbcTemplate) {
		this.shardJdbcTemplate = shardJdbcTemplate;
	}

	@Override
	public void updatePlan(Plan plan) {
		String sql = "update plan set name=?,accountid=?, createdate=? where planid=?";
		Object[] args = this.convertToUpdateArgs(plan);
		
		shardJdbcTemplate.update(sql, args);
	}

	@Override
	public void updatePlans(List<Plan> planList) {
		String sql = "update plan set name=?, accountid=?, createdate=? where planid=?";
		List<Object[]> args = new ArrayList<Object[]>();
		
		for (Plan plan : planList) {
			args.add(this.convertToUpdateArgs(plan));
		}
		
		shardJdbcTemplate.batchUpdate(sql, args);
	}
	
	private Object[] convertToUpdateArgs(Plan plan) {
		Object[] args = {
				plan.getName(),
				plan.getAccountId(),
				plan.getCreateDate(),
				plan.getPlanId()
			};
		
		return args;
		
	}

}
