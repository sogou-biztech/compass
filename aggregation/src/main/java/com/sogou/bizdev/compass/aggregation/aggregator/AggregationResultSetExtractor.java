package com.sogou.bizdev.compass.aggregation.aggregator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import com.sogou.bizdev.compass.aggregation.aggregator.collector.GroupListCollector;


/**
 * ResultSetExtractor的聚合实现类
 *
 * @author yk
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class AggregationResultSetExtractor implements ResultSetExtractor {
	
	private AggregationDescriptor descriptor;
	
	public AggregationResultSetExtractor(AggregationDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public GroupListCollector extractData(ResultSet rs) throws SQLException, DataAccessException {
		try {
			GroupListCollector groupListCollector = new GroupListCollector(descriptor);
			Map<String, Integer> fieldIndex = this.createFieldIndex(rs);
			ResultSetAccessor accessor = new ResultSetAccessor(rs, fieldIndex);

			while (rs.next()) {
				groupListCollector.collectRow(accessor);
			}
			
			return groupListCollector;
		} catch (SQLExceptionCarrier e) {
			throw e.getException();
		}
	}
	
	private Map<String, Integer> createFieldIndex(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Integer> fieldIndex = new LinkedHashMap<String, Integer>(columnCount);

		for (int i = 1; i <= columnCount; i++) {
			String field = JdbcUtils.lookupColumnName(rsmd, i);
			fieldIndex.put(field, Integer.valueOf(i));
		}

		return fieldIndex;
	}
	
	/**
	 * 用于屏蔽ResultSetAccessor中从ResultSet取值时可能发生的SQLException
	 * 在与Spring JdbcTemplate交互的边界应当将此SQLException抛出以供JdbcTemplate处理
	 * @author yanke
	 *
	 */
	public static class SQLExceptionCarrier extends RuntimeException {
		
		private static final long serialVersionUID = 3274626377702144687L;

		public SQLExceptionCarrier(SQLException e) {
			super(e);
		}
		
		public SQLException getException() {
			return (SQLException) super.getCause();
		}
		
	}

}
