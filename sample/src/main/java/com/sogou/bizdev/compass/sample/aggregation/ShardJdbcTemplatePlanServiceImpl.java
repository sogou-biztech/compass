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
		String sql = "select * from cpcplan where cpcplanid=?";
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
		String sql = "select * from cpcplan";
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
			int offset = pageCount * pageSize;
			int rows = pageSize;
			
			String sql = "select * from cpcplan";
			AggregationDescriptor descriptor = new AggregationDescriptor()
				.orderBy("cpcplan_id", true)
				.limit(offset, rows);
			
			shardJdbcTemplate.setMaxRows(1000000);
			return shardJdbcTemplate.query(sql, null, this.getAggregatedPlanMapper(), descriptor);
		} finally {
			shardJdbcTemplate.setMaxRows(originMaxRows);
		}
	}

	@Override
	public List<Plan> queryPlansByAccountIds(List<Long> accountIds) {
		String sql = "select * from cpcplan where accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")";
		Object[] args = new Object[] { accountIds };
		PlanMapper mapper = new PlanMapper();

		return shardJdbcTemplate.query(sql, args, mapper);
	}

	@Override
	public List<Plan> queryPlansByAccountIdsAndTime(List<Long> accountIds, String startTime, String endTime) {
		String sql = "select * from cpcplan where"
				+ " createdate > ?"
				+ " and accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")"
				+ " and createdate < ?";
		Object[] args = new Object[] { startTime, accountIds, endTime };
		PlanMapper mapper = new PlanMapper();

		return shardJdbcTemplate.query(sql, args, mapper);
	}

	@Override
	public List<PlanCount> countPlanOfAccounts(List<Long> accountIds) {
		String sql = "select cpcplanid, accountid from cpcplan where accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")";
		Object[] args = new Object[] { accountIds };
		AggregationDescriptor descriptor = new AggregationDescriptor()
			.count("cpcplanid", "cpcplanidCount")
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
                planCount.setPlanCount((Integer) aggregatedRow.get("cpcplanidCount"));
                
                return planCount;
			}
		};
	}
	
	public AggregatedRowMapper<Plan> getAggregatedPlanMapper() {
		return new AggregatedRowMapper<Plan>() {

			@Override
			public Plan mapRow(Map<String, Object> row) {
				Plan plan = new Plan();
				
				plan.setCpcplanid((Long) row.get("cpcplanid"));
				plan.setName((String) row.get("name"));
				plan.setIspause((Integer) row.get("ispause"));
				plan.setAccountid((Long) row.get("accountid"));
				plan.setCreatedate((Date) row.get("createdate"));
				plan.setChgdate((Date) row.get("chgdate"));
				plan.setStartDate((Date) row.get("start_date"));
				plan.setEndDate((Date) row.get("end_date"));
				
				return plan;
			};
		};
	}
	
	@Override
	public List<PlanCount> countAllPlanOfAccounts() {
		int originMaxRows = shardJdbcTemplate.getMaxRows();
		try {
			String sql = "select cpcplanid, accountid from cpcplan";
			AggregationDescriptor descriptor = new AggregationDescriptor()
				.count("cpcplanid", "cpcplanidCount")
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
		String sql = "update cpcplan set name=?, ispause=?, accountid=?, createdate=?, chgdate=?, start_date=?, end_date=? where cpcplanid=?";
		Object[] args = this.convertToUpdateArgs(plan);
		
		shardJdbcTemplate.update(sql, args);
	}

	@Override
	public void updatePlans(List<Plan> planList) {
		String sql = "update cpcplan set name=?, ispause=?, accountid=?, createdate=?, chgdate=?, start_date=?, end_date=? where cpcplanid=?";
		List<Object[]> args = new ArrayList<Object[]>();
		
		for (Plan plan : planList) {
			args.add(this.convertToUpdateArgs(plan));
		}
		
		shardJdbcTemplate.batchUpdate(sql, args);
	}
	
	private Object[] convertToUpdateArgs(Plan plan) {
		Object[] args = {
				plan.getName(),
				plan.getIspause(),
				plan.getAccountid(),
				plan.getCreatedate(),
				plan.getChgdate(),
				plan.getStartDate(),
				plan.getEndDate(),
				plan.getCpcplanid()
			};
		
		return args;
		
	}

}
