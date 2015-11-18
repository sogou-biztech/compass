package com.sogou.bizdev.compass.sample.aggregation;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;
import com.sogou.bizdev.compass.aggregation.template.ShardJdbcTemplate;

@ContextConfiguration(locations = { 
	"classpath*:/conf/aggregation/test-aggregation-shardjdbctemplate.xml",
	"classpath*:/datasource/shard/test-shard-*.xml" 
})
/**
 * 扫描全表查询示例：
 * 查询指定日期内的全部cpcplan、进行排序或聚合
 * 
 * @author yk
 *
 */
public class FullScanQueryTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ShardJdbcTemplate shardJdbcTemplate;
	
	private String sql = "select * from plan where createdate between ? and ?";
	private Object[] args = new Object[] {
								"2012-09-01",
								"2012-12-31"
							};
	
	/**
	 * 简单查询
	 */
	@Test
	public void simpleQuery() {
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planid") + ", " + row.get("name"));
		}
	}
	
	/**
	 * 按照accountid升序排序查询
	 */
	@Test
	public void orderedQuery() {
		// 结合以下descriptor的查询等效于：
		// select * from cpcplan where createdate between ? and ? order by accountid asc
		AggregationDescriptor descriptor = new AggregationDescriptor();
		descriptor.orderBy("accountid", true);
		
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args, descriptor);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planid") + ", " + row.get("name"));
		}
	}
	
	/**
	 * 聚合查询
	 * 按照accountid分组、计算每个accountid下的cpcplanid数目
	 * 并且按照accountid升序排序
	 * 
	 */
	@Test
	public void aggregatedQuery() {
		// 结合以下descriptor的查询等效于：
		// select cpcplan.*, count(cpcplanid) as cpcplanidCount from cpcplan where createdate between ? and ? group by accountid order by accountid asc
		AggregationDescriptor descriptor = new AggregationDescriptor();
		descriptor.groupBy("accountid")
					.count("planid", "planidCount")	
					.orderBy("accountid", true);
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args, descriptor);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planidCount"));
		}
	}

}
