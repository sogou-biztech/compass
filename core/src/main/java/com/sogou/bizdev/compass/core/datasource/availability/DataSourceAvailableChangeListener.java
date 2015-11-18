package com.sogou.bizdev.compass.core.datasource.availability;

/**
 * 数据库心跳监听器
 * @author gly
 * @since 1.0.0
 */
public interface DataSourceAvailableChangeListener {
	
	enum Status {AVAILABLE, UNAVAILABLE}
	
	/**监听数据源状态变化
	 * @param dataSourceId
	 * @param status
	 */
	void availableChanged(String dataSourceId, Status status);
	
}
