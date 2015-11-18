package com.sogou.bizdev.compass.aggregation.template;

/**
 * ShardJdbcTemplate的一些运行参数的配置
 * 
 * @author yk
 * @since 1.0.0
 */
public class ShardJdbcConfig {
	
	/**
	 * 针对每个分库数据源进行查询时的线程池大小
	 */
	private int threadPoolSize = Runtime.getRuntime().availableProcessors() * 5;
	
	/**
	 * 数据库查询以及主线程等待并发结果时的超时时间。单位为秒
	 */
	private int queryTimeout = 300;
	
	/**
	 * 进行查询时的最大结果集数目
	 * 超过此数目将抛出SqlExecutionFailureException
	 */
	private int maxRows = 1000000;
	
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	public int getQueryTimeout() {
		return queryTimeout;
	}
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}
	public int getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(int maxRows) {
		if (maxRows < 0) {
			throw new IllegalArgumentException("invalid value for maxRows: " + maxRows + ", a valid value should be no less than zero");
		}
		this.maxRows = maxRows;
	}

}
