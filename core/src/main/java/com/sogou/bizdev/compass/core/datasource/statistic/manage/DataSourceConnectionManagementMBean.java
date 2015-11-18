package com.sogou.bizdev.compass.core.datasource.statistic.manage;

import java.util.List;

import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;

/**
 * 数据源连接池监控JMX
 * 
 * @author gly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DataSourceConnectionManagementMBean {
	
	public List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics(String datasourceId);
 
}