package com.sogou.bizdev.compass.core.datasource.availability.tester;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizdev.compass.core.datasource.availability.DatabaseDetectingEvent;

/**
 * 数据库探测器(含重试)
 * 
 * @author gly
 * @since 1.0.0
 */
public abstract class AbstractDatabaseAliveTester implements DatabaseAliveTester
{
    private Logger logger = LoggerFactory.getLogger(getClass());  
    
	private static final int DEFAULT_RETRY_TIMES = 3;//默认retry次数为3次

	private static final int DEFAULT_RETRY_INTERVALS = 1000; //默认每次探测失败后，线程Sleep1s
	
	private static final int DEFAULT_QUERY_TIME_OUT=5;//默认每次查询超时时间为5s
	
	/**
	 * 心跳探测重试次数
	 */
	private int retryTimes = DEFAULT_RETRY_TIMES;
	
	/**
	 * 心跳探测重试间隔
	 */
	private int retryIntervals = DEFAULT_RETRY_INTERVALS; 
	
	/**
	 * 探测超时时间
	 */
	private int queryTimeOut=DEFAULT_QUERY_TIME_OUT;

	private ThreadLocal<Throwable> lastExceptionHolder = new ThreadLocal<Throwable>();

	protected abstract String getDefaultPingSql();
	
	@Override
	public boolean isDatabaseAlive(DatabaseDetectingEvent event) {
		int failTimes = 0;
		int retryTimes = getRetryTimes();
		int retryIntervals = getRetryIntervals();
		while (failTimes<retryTimes) {
			boolean alive = isAlive(event);
			if (alive) {
				if (failTimes>0 && logger.isDebugEnabled()) {
					logger.debug("Retried '"+failTimes+"' times, last execption is "+lastExceptionHolder.get(), lastExceptionHolder.get());
				}
				return true;
			} else {
				failTimes++;
				try {
					Thread.sleep(retryIntervals);
				} catch (InterruptedException e) {
				}
			}
		}
		logger.error("Data source '"+event.getDataSource().getId()+"' is unavailable, last execption is "+lastExceptionHolder.get(), lastExceptionHolder.get());
		return false;
	}

    protected boolean isAlive(DatabaseDetectingEvent event)
    {
    	Connection connection = null;
    	PreparedStatement statement = null;
		DataSource dataSource = event.getDataSource();
		String pingSql = null;
		try
		{
			connection = dataSource.getConnection();
			pingSql = event.getPingSql();
			if (pingSql == null)
			{
				pingSql = getDefaultPingSql();
			}
			statement = connection.prepareStatement(pingSql);
			statement.setQueryTimeout(getQueryTimeOut());
			statement.execute();
			return true;
		} 
		catch (Throwable t) 
		{
			lastExceptionHolder.set(t);
			if (logger.isDebugEnabled())
			{
				logger.debug("Database '"+event.getDataSource().getId()+"' is unavailable, execption is "+lastExceptionHolder.get(), t);
			}
			return handleException(t);
		} 
		finally 
		{

			if (statement!=null)
			{
				try
				{
					statement.close();
				}
				catch(Throwable ex)
				{
                    logger.error("close statement error!", ex);
				}
			}
			if ( connection != null) 
			{
				try
				{
					connection.close();
				}
				catch(Throwable ex)
				{
					logger.error("close connection error!", ex);
				}
			}

		}
	}

	protected boolean handleException(Throwable t)
	{
		return false;
	}

    public int getRetryTimes()
    {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) 
	{
		this.retryTimes = retryTimes;
	}

	public int getRetryIntervals()
	{
		return retryIntervals;
	}

	public void setRetryIntervals(int retryIntervals)
	{
		this.retryIntervals = retryIntervals;
	}

	public int getQueryTimeOut()
	{
		return queryTimeOut;
	}

	public void setQueryTimeOut(int queryTimeOut)
	{
		this.queryTimeOut = queryTimeOut;
	}
}
