package com.sogou.bizdev.compass.core.datasource.availability.tester;


/**
 * Mysql数据库探测器
 * 
 * @author gly
 * @since 1.0.0
 */
public class MysqlAliveTester extends AbstractDatabaseAliveTester {
    
    protected String getDefaultPingSql() {
    	return "select 1";
    }
}
