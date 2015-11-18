package com.sogou.bizdev.compass.core.datasource;

import java.util.List;

import javax.sql.DataSource;

import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;

/**
 * 抽象数据源
 * 
 * @author gly
 * @since 1.0.0
 */
public abstract class AbstractDataSource implements DataSource 
{

	/**
	 * 获取数据库连接池监控信息
	 * @return 当前数据源所有真实数据库连接池信息
	 */
	public abstract List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics();
	
	private String id;

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}
	
	


}
