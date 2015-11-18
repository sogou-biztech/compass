package com.sogou.bizdev.compass.aggregation.aggregator;

import java.util.Map;

/**
 * 聚合结果集映射器
 * 用于将Map<String, Object>形式的聚合结果映射成指定类型的对象
 * 
 * 注：参数Map的key是case insensitive的
 *
 * @author yanke@sogou-inc.com
 * @since 1.0.0
 */
public interface AggregatedRowMapper<T> {
	
	public T mapRow(Map<String, Object> aggregatedRow);

}
