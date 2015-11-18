package com.sogou.bizdev.compass.sample.aggregation;

import java.util.Arrays;
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
 * 指定范围查询示例：
 * 查询指定的accountid范围内的cpcplan
 * 
 * "指定范围查询"共支持三种参数形式，可分别参见sample1(), sample2(), sample3()
 * "指定范围查询"与"聚合查询"结合使用的示例，请参见aggregatedSample()
 * 
 * @author yk
 *
 */
public class RangedQueryTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ShardJdbcTemplate shardJdbcTemplate;
	
	// 需要使用ShardJdbcTemplate.RANGE_PLACEHOLDER替换掉sql的in语句中的?
	private String sql = "select accountid, cpcplanid, name from cpcplan where"
							+ " createdate > ?"
							+ " and accountid in (" + ShardJdbcTemplate.RANGE_PLACEHOLDER + ")"
							+ " and createdate < ?";
	private String startDate = "2012-07-01";
	private String endDate = "2013-07-01";
	
	/**
	 * 第一种参数形式
	 * 每一个accountid均作为参数数组中的一个元素
	 */
	@Test
	public void sample1() {
		Object[] args =new Object[this.getAccountIds().length+2];
		System.arraycopy(this.getAccountIds(), 0, args, 0, this.getAccountIds().length);  
		args[this.getAccountIds().length]=startDate;
		args[this.getAccountIds().length+1]=endDate; 
		
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planid") + ", " + row.get("name"));
		}
	}
	
	/**
	 * 第二种参数形式
	 * 所有的accountid以Object[]形式作为参数数组中的一个元素
	 */
	@Test
	public void sample2() {
		Object[] args = new Object[] { startDate, this.getAccountIds(), endDate };
		
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planid") + ", " + row.get("name"));
		}
	}
	
	/**
	 * 第三种参数形式
	 * 所有的accountid以List<Object>形式作为参数数组中的一个元素
	 */
	@Test
	public void sample3() {
		Object[] args = new Object[] { startDate, this.getAccountIdsAsList(), endDate };
		
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planid") + ", " + row.get("name"));
		}
		
	}
	
	/**
	 * 指定范围查询与聚合查询结合使用
	 * 
	 * 查询指定accountid范围的cpcplan，按照accountid分组并计算cpcplanid数目，然后按照accountid升序排序
	 */
	@Test
	public void aggregatedSample() {
		// 指定范围查询除需要用RANGE_PLACEHOLDER替换掉sql的in语句中的?以外，并无特殊
		// 因此同样可以与聚合描述器结合
		AggregationDescriptor descriptor = new AggregationDescriptor()
						.count("cpcplanid", "planidCount")
						.groupBy("accountid")
						.orderBy("accountid", true);
		
		Object[] args = new Object[] { startDate, this.getAccountIdsAsList(), endDate };
		
		List<Map<String, Object>> resultList = shardJdbcTemplate.query(sql, args, descriptor);
		
		for (Map<String, Object> row : resultList) {
			System.out.println(row.get("accountid") + ", " + row.get("planidCount"));
		}
	}
	
	
	private Object[] getAccountIds() {
		Object[] args = new Object[] { 291392L, 300032L, 322688L, 331456L, 293640L, 298440L, 306888L, 312648L, 313608L,
				323336L, 344776L, 351816L, 355720L, 421705L, 423497L, 425033L, 426377L, 316561L, 319185L, 336465L,
				343953L, 345489L, 352209L, 353105L, 356369L, 357969L, 364817L };

		return args;
	}
	
	private List<Object> getAccountIdsAsList() {
		return Arrays.asList(this.getAccountIds());
	}

}
