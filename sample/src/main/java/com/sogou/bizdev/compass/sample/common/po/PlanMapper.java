package com.sogou.bizdev.compass.sample.common.po;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PlanMapper implements RowMapper {
	
	public static String[] mappedDbFields = new String[] {
		"planid",
		"name",
		"accountid",
		"createdate",
	};

	@Override
	public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
		Plan plan = new Plan();
		
		plan.setPlanId(rs.getLong("cpcplanid"));
		plan.setName(rs.getString("name"));
		plan.setAccountId(rs.getLong("accountid"));
		plan.setCreateDate(rs.getDate("createdate"));
		return plan;
	}
	
}