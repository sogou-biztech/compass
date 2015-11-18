package com.sogou.bizdev.compass.core.datasource.statistic.manage;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;

import com.sogou.bizdev.compass.core.datasource.AbstractDataSource;
import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;

/**
 * 数据源连接池监控JMX实现
 * @author gly
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataSourceConnectionManagement extends ApplicationObjectSupport implements
		DataSourceConnectionManagementMBean {
	private static Logger logger = Logger.getLogger(DataSourceConnectionManagement.class);  
	
	
	@Override
	public List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics(
			String dataSourceId) {
		List<DataSourceConnectionStatistic>  connectionStatistics=new ArrayList<DataSourceConnectionStatistic>();
		try {
			ApplicationContext applicationContext=this.getApplicationContext();
			DataSource dataSource=(DataSource)applicationContext.getBean(dataSourceId);
			if(dataSource instanceof AbstractDataSource){
				connectionStatistics.addAll(((AbstractDataSource)dataSource).getDataSourceConnectionStatistics());
			} 
		} catch (Exception e) {
			logger.error("can't parse datasourceId to DataSource Bean,please check datasourceId",e);
		}
		
		return connectionStatistics;
	}
	
	 
}