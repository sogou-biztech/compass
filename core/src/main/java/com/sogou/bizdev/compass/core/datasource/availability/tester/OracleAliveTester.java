package com.sogou.bizdev.compass.core.datasource.availability.tester;

/**
 * Oracle数据库探测器
 * 
 * @author gly
 * @since 1.0.0
 */
public class OracleAliveTester extends AbstractDatabaseAliveTester {

	@Override
	protected String getDefaultPingSql() {
		return "select 1 from dual";
	}

}
