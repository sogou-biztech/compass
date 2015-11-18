package com.sogou.bizdev.compass.sample.common.po;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PlanMapper implements RowMapper {
	
	public static String[] mappedDbFields = new String[] {
		"cpcplanid",
		"name",
		"ispause",
		"accountid",
		"createdate",
		"chgdate",
		"start_date",
		"end_date"
	};

	@Override
	public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
		Plan plan = new Plan();
		
		plan.setCpcplanid(rs.getLong("cpcplanid"));
		plan.setName(rs.getString("name"));
		plan.setIspause(rs.getInt("ispause"));
		plan.setAccountid(rs.getLong("accountid"));
		plan.setCreatedate(rs.getDate("createdate"));
		plan.setChgdate(rs.getDate("chgdate"));
		plan.setStartDate(rs.getDate("start_date"));
		plan.setEndDate(rs.getDate("end_date"));
		
		return plan;
	}
	
}