package com.sogou.bizdev.compass.aggregation.aggregator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.support.JdbcUtils;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationResultSetExtractor.SQLExceptionCarrier;
import com.sogou.bizdev.compass.aggregation.aggregator.collector.RowAccessor;

/**
 * 查询结果行的包装器
 * 提供了基于field的取值功能
 *
 * @author yk
 * @since 1.0.0
 *
 */
public class ResultSetAccessor implements RowAccessor {

	private Map<String, Integer> fieldIndex;
	private ResultSet resultSet;
	
	public ResultSetAccessor(ResultSet resultSet, Map<String, Integer> fieldIndex) {
		this.resultSet = resultSet;
		this.fieldIndex = fieldIndex;
	}

	public Object getFieldValue(String field)  {
		Integer index = fieldIndex.get(field);
		
		if (index == null || index.intValue() < 0) {
			throw new IllegalArgumentException("invalid field=" + field);
		}
		
		try {
			return JdbcUtils.getResultSetValue(resultSet, index);
		} catch (SQLException e) {
			throw new SQLExceptionCarrier(e);
		}
    }
	
	public Map<String, Object> toFieldValueMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for (Entry<String, Integer> entry : fieldIndex.entrySet()) {
			String field = entry.getKey();
			Object value = getFieldValue(field);
			
			map.put(field, value);
		}
		
		return map;
	}


}
