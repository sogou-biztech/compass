package com.sogou.bizdev.compass.core.datasource;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.util.Assert;

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
	
	private PrintWriter logWriter;

	private int loginTimeout;

	
    @Override
	public PrintWriter getLogWriter() throws SQLException
    {
		return logWriter;
	}

    @Override
	public void setLogWriter(PrintWriter logWriter) throws SQLException
    {
		this.logWriter = logWriter;
	}

	@Override
	public int getLoginTimeout() throws SQLException
	{
		return loginTimeout;
	}

	@Override
	public void setLoginTimeout(int loginTimeout) throws SQLException
	{
		this.loginTimeout = loginTimeout;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException 
	{
		Assert.notNull(iface, "Interface argument must not be null");
		if (!(DataSource.class.equals(iface)))
		{
			throw new SQLException(
					"DataSource of type ["+ super.getClass().getName()+ "] can only be unwrapped as [javax.sql.DataSource], not as ["+ iface.getName()+"]");
		}
		return (T) this;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException 
	{
		return DataSource.class.equals(iface);

	}

	//---------------------------------------------------------------------
	// Implementation of JDBC 4.1's getParentLogger method
	//---------------------------------------------------------------------

	public Logger getParentLogger()
	{
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	


}
