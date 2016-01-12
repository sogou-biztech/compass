package com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao.ShardPlanDao;

public class ShardPlanDaoImpl implements ShardPlanDao {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Plan createPlan(Long accountId,Plan plan) {
		StringBuilder sql = new StringBuilder("insert into plan(planid,accountid,name,createdate) values(?,?,?,?) ");
		List params = new ArrayList(); 
		params.add(plan.getPlanId());
		params.add(plan.getAccountId());
		params.add(plan.getName());
		params.add(new Date()); 
		
		getJdbcTemplate().update(sql.toString(), params.toArray());
		
		return getPlanByPlanId(accountId, plan.getPlanId());
		
	}
	
	@Override
	public List<Plan> getPlansByAccountId(Long accountId) {
		String sql = "select accountid, planid, name,createdate from plan where accountid=?";
		
		List<Plan> plans = getJdbcTemplate().query(sql, new Object[]{accountId}, new RowMapper<Plan>(){

			@Override
			public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Plan p = new Plan();
				p.setAccountId(rs.getLong(1));
				p.setPlanId(rs.getLong(2));
				p.setName(rs.getString(3));
				p.setCreateDate(rs.getDate(4));
				return p;
			}
			
		});
				
		return plans;
	}

	@Override
	public Plan updatePlan(Long accountId, Plan p) {
		StringBuilder sql = new StringBuilder("update plan set ");
		List params = new ArrayList(); 
		if(p.getName() != null){
			sql.append(" name=? ");
			params.add(p.getName());
		}
		sql.append(" where accountid=? and planid=?");
		params.add(accountId);
		params.add(p.getPlanId());
		
		getJdbcTemplate().update(sql.toString(), params.toArray());
		
		return getPlanByPlanId(accountId, p.getPlanId());
	}
 
	public Plan getPlanByPlanId(Long accountId, Long planId) {
		String sql = "select accountid, planid, name,createdate from plan where accountid=? and planid=?";
		
		List<Plan> plans = getJdbcTemplate().query(sql, new Object[]{accountId, planId}, new RowMapper<Plan>(){

			@Override
			public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Plan p = new Plan();
				p.setAccountId(rs.getLong(1));
				p.setPlanId(rs.getLong(2));
				p.setName(rs.getString(3));
				p.setCreateDate(rs.getDate(4));
				return p;
			}
			
		});
				
		return CollectionUtils.isEmpty(plans) ? null:plans.get(0);
	}

	
	
	
}
