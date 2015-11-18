package com.sogou.bizdev.compass.sample.jdbctemplate.shard.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
	public List<Plan> getPlansById(Long accountId) {
		String sql = "select accountid, cpcplanid, name from cpcplan where accountid=?";
		
		List<Plan> plans = getJdbcTemplate().query(sql, new Object[]{accountId}, new RowMapper<Plan>(){

			@Override
			public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Plan p = new Plan();
				p.setAccountid(rs.getLong(1));
				p.setCpcplanid(rs.getLong(2));
				p.setName(rs.getString(3));
				return p;
			}
			
		});
				
		return plans;
	}

	@Override
	public Plan updatePlanById(Long accountId, Plan p) {
		StringBuilder sql = new StringBuilder("update cpcplan set ");
		List params = new ArrayList(); 
		if(p.getIspause() != null) {
			sql.append(" ispause=? ");
			params.add(p.getIspause());
		}
		if(p.getName() != null){
			sql.append(", name=?");
			params.add(p.getName());
		}
		sql.append(" where accountid=? and cpcplanid=?");
		params.add(accountId);
		params.add(p.getCpcplanid());
		
		getJdbcTemplate().update(sql.toString(), params.toArray());
		
		return getPlanById(accountId, p.getCpcplanid());
	}
 
	public Plan getPlanById(Long accountId, Long planId) {
		String sql = "select accountid, cpcplanid, name from cpcplan where accountid=? and cpcplanid=?";
		
		List<Plan> plans = getJdbcTemplate().query(sql, new Object[]{accountId, planId}, new RowMapper<Plan>(){

			@Override
			public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Plan p = new Plan();
				p.setAccountid(rs.getLong(1));
				p.setCpcplanid(rs.getLong(2));
				p.setName(rs.getString(3));
				return p;
			}
			
		});
				
		return CollectionUtils.isNotEmpty(plans) ? plans.get(0) : null;
	}
	
	
}
