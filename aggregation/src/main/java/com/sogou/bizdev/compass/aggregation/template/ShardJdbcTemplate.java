package com.sogou.bizdev.compass.aggregation.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.util.Assert;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregatedRowMapper;
import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;
import com.sogou.bizdev.compass.aggregation.aggregator.AggregationResultSetExtractor;
import com.sogou.bizdev.compass.aggregation.aggregator.collector.GroupListCollector;
import com.sogou.bizdev.compass.aggregation.concurrency.ShardAwareCallable;
import com.sogou.bizdev.compass.aggregation.concurrency.ShardAwareCallableExecutor;
import com.sogou.bizdev.compass.aggregation.datasource.BatchRouteKeyRouter;
import com.sogou.bizdev.compass.aggregation.datasource.Shard;
import com.sogou.bizdev.compass.aggregation.exception.SqlExecutionFailureException;
import com.sogou.bizdev.compass.aggregation.util.AggregationUtil;
import com.sogou.bizdev.compass.aggregation.util.RangedSqlUtil;
import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * 聚合查询的模版类，提供了ShardJdbcOperations中定义的所有方法的实现
 * 需要注入一个ShardDataSource
 * 支持在初始化阶段对如下参数进行设置：
 *    threadPoolSize: 并发查询时每个数据源对应的thread pool的最大线程数目
 *    queryTimeout:   数据库查询以及主线程等待并发结果时的timeout参数
 *    maxRows:        进行查询操作时的最大结果集数目
 *    
 * 
 * 对于除batchUpdate()方法以外的所有update()方法和query()方法，支持两种形式的查询或更新操作：
 * 1. 未指定分表依据的全分表遍历操作。例如：
 *    select * from foo where foo_id = ?
 *    update foo set bar = ? where foo_id = ?
 *    delete from foo where foo_id in (?, ?, ?, ?, ?)
 *    注：foo_id不是分表依据字段
 * 
 *    对于此类操作，只要使用与Spring JdbcTemplate相同的方式构建sql、args即可
 *    ShardJdbcTemplate中会遍历所有分表、执行相同的sql，然后将每个分表的执行结果合并后返回给使用者
 *   
 * 2. 指定一组分表依据的操作。例如：
 *    select * from foo where foo_field1=? and bar_id in (?, ?, ?, .... ) and foo_field2=?
 *    其中bar_id为分表依据字段
 * 
 *    对于此类操作，ShardJdbcTemplate中做了一些优化，可以做到不用遍历所有分表，只操作涉及到的分表
 *    为了享受到这些优化，需要使用特殊的方式构造sql语句和参数
 *    2.1 构造语句的方法：使用ShardJdbcTemplate.RANGE_PLACEHOLDER代替in语句中的占位符，例如：
 *        select * from foo where foo_field1=? and bar_id in (ShardJdbcTemplate.RANGE_PLACEHOLDER) and foo_field2=?
 * 
 *    2.2 构造参数的方法：
 *        以下三种方式均可
 *        1) 将分表依据的每一个值作为参数中的一个成员
 *           Object[] args = new Object[] { foo_field1_value, bar_id1, bar_id2, bar_id3, bar_id4.... , foo_field2_value };
 *        2) 将分表依据数组整体作为参数中的一个成员, 与RANGE_PLACEHOLDER在sql参数占位符中的位置对应
 *           Object[] args = new Object[] { foo_field1_value, (an Object[] of bar_id(s)), foo_field2_value };
 *        3) 将分表依据List整体作为参数中的一个成员，与RANGE_PLACEHOLDER在sql参数占位符中的位置对应
 *           Object[] args = new Object[] { foo_field1_value, (an List<T> of bar_id(s)) , foo_field2_value };
 *    
 * 注：如果没有使用上述特殊方式构造sql和参数，那么ShardJdbcTemplate会以形式1中的全分表扫描方式进行操作。
 * 注1：只有用分表依据字段作为参数时，才支持形式2。对于非分表依据字段的批量操作，请使用形式1中的方式进行操作。
 * 
 * 
 * @author yk | kurtyan777@gmail.com
 * @since 1.0.0
 */
public class ShardJdbcTemplate extends ShardJdbcConfig implements ShardJdbcOperations, InitializingBean, DisposableBean {
	
	/**
	 * 指定范围查询sql的in ()部分中需要使用此常量代替常规sql使用的?, ?形式的占位符
	 * 如： select * from foo where bar in (RAGNE_PLACEHOLDER)
	 */
    public final static String RANGE_PLACEHOLDER = "${rangePlaceHolder}";

    /**
     * 分库数据源
     */
    private ShardDataSource shardDataSource;
    
    /**
     * 并发操作执行器
     */
    private ShardAwareCallableExecutor executor;
    
    /**
     * 指定范围查询场景下对routeEvidenceList进行路由的路由器
     */
    private BatchRouteKeyRouter batchKeyRouter;

    /**
     * 用于解析指定范围查询的sql和参数
     */
    private RangedOperationParser rangedOperationParser = new RangedOperationParser();

    public ShardJdbcTemplate(ShardDataSource shardDataSource) {
    	Assert.notNull(shardDataSource, "shardDataSource must not be null");
        this.shardDataSource = shardDataSource;
        this.batchKeyRouter = new BatchRouteKeyRouter(shardDataSource);
    }

    /**
     * 创建一个JdbcTemplate，并且设置超时、maxRows参数
     * @param dataSource
     * @param config
     * @return
     */
    private JdbcTemplate createJdbcTemplate(DataSource dataSource, ShardJdbcConfig config) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(config.getQueryTimeout());
        jdbcTemplate.setMaxRows(config.getMaxRows());
        
        return jdbcTemplate;
    }

    /**
     * 提交给query和update方法的sql和args分为两种类型
     * 1. 需要全表扫描
     * 2. 需要根据一组routeEvidence进行分表查询
     *
     * 为了屏蔽上述两种类型间处理分表逻辑的差异，
     * 分表逻辑被抽取到了此方法中、对分表的处理操作被抽象成ShardOperationProcessor接口
     *
     * 使用者无需关心sql和args是需要全表扫描还是分表查询，只要提交sql、args和一个processor给此方法即可
     *
     * @see com.sogou.bizdev.compass.aggregation.template.ShardJdbcTemplate.ShardOperationProcessor
     * @param sql
     * @param args
     * @param processor
     * @param <T>
     * @return
     */
    private <T> T process(String sql, Object[] args, ShardOperationProcessor<T> processor) 
    {
        if (rangedOperationParser.isRangedOperation(sql))
        {	// 需要根据一组routeEvidence进行分表查询
            SqlParameterList parameterList = rangedOperationParser.parseParameter(sql, args);
            List<Object> routeKeys = parameterList.getRangeParameterList();
            Map<TableContext,Shard> dataSourceEvidenceMap = batchKeyRouter.route(routeKeys);

            for (Map.Entry<TableContext,Shard> entry : dataSourceEvidenceMap.entrySet()) 
            {
                Shard shard = entry.getValue();
                List<Object> groupedRouteKeys=shard.getRouteKeys();
                String replacedSql = sql.replace(RANGE_PLACEHOLDER, RangedSqlUtil.buildPlaceHolder(groupedRouteKeys.size()));
                Object[] actualArgs = parameterList.buildFullParameterList(groupedRouteKeys);

                processor.addOperation(shard, replacedSql, actualArgs);
            }
        } 
        else
        {	// 需要扫描所有分表的查询
            List<Shard> shards = batchKeyRouter.listAllShards();

            for (Shard shard : shards) {
                processor.addOperation(shard, sql, args);
            }
        }

        return processor.processOperations();
    }

    @Override
    public int update(final String sql, final Object[] args) {
		Assert.notNull(sql, "sql must not be null");
		
        final List<ShardAwareCallable<Integer>> callableList = new ArrayList<ShardAwareCallable<Integer>>();

        ShardOperationProcessor<Integer> processor = new ShardOperationProcessor<Integer>() {
            @Override
            public void addOperation(Shard shard, String sql, Object[] args) {
                ShardAwareCallable<Integer> callable = new UpdateCallable(shard, sql, args);
                callableList.add(callable);
            }

            @Override
            public Integer processOperations() {
                List<Integer> integerList = executor.execute(callableList);
                return AggregationUtil.aggregateInteger(integerList);
            }
        };


        return this.process(sql, args, processor);
    }
    
    @Override
    public int[] batchUpdate(String sql, List<Object[]> args)
    {
		Assert.notNull(sql, "sql must not be null");
		Assert.notEmpty(args, "arg list must not be empty");

        List<Shard> shards = batchKeyRouter.listAllShards();

        List<ShardAwareCallable<int[]>> callableList = new ArrayList<ShardAwareCallable<int[]>>();
        for (final Shard shard : shards)
        {
            ShardAwareCallable<int[]> callable = new BatchUpdateCallable(shard, sql, args);
            callableList.add(callable);
        }

        List<int[]> intsList = executor.execute(callableList);
        return AggregationUtil.aggregateIntegerArray(intsList);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<Map<String, Object>> query(String sql, Object[] args) {
        return this.query(sql, args, new ColumnMapRowMapper());
    }
    
	@Override
    public <T> List<T> query(String sql, Object[] args, final RowMapper<T> rowMapper) {
		Assert.notNull(sql, "sql must not be null");
		Assert.notNull(rowMapper, "rowMapper must not be null");

        ShardOperationProcessor<List<T>> processor = this.getNonAggregationProcessor(rowMapper);
        return this.process(sql, args, processor);
    }

    /**
     * 获取一个用于处理聚合查询的ShardOperationProcessor
     * @param descriptor
     * @return
     */
    private ShardOperationProcessor<List<Map<String, Object>>> getAggregationProcessor(final AggregationDescriptor descriptor) {
        final List<QueryCallable<GroupListCollector>> callableList = new ArrayList<ShardJdbcTemplate.QueryCallable<GroupListCollector>>();

        ShardOperationProcessor<List<Map<String, Object>>> processor = new ShardOperationProcessor<List<Map<String, Object>>>() {
            @Override
            public void addOperation(Shard shard, String sql, Object[] args) {
            	AggregationResultSetExtractor extractor = new AggregationResultSetExtractor(descriptor);
                QueryCallable<GroupListCollector> callable = new QueryCallable<GroupListCollector>(shard, sql, args, extractor);
                callableList.add(callable);
            }

            @Override
            public List<Map<String, Object>> processOperations() {
                List<GroupListCollector> groupList = ShardJdbcTemplate.this.executeQuery(callableList);
                return AggregationUtil.aggregateGroupList(groupList);
            }
        };

        return processor;
    }

	private <T> ShardOperationProcessor<List<T>> getNonAggregationProcessor(final RowMapper<T> rowMapper) {
        final List<QueryCallable<List<T>>> callableList = new ArrayList<ShardJdbcTemplate.QueryCallable<List<T>>>();

        ShardOperationProcessor<List<T>> processor = new ShardOperationProcessor<List<T>>() {
            @SuppressWarnings("unchecked")
			@Override
            public void addOperation(Shard shard, String sql, Object[] args) {
                ResultSetExtractor extractor = new RowMapperResultSetExtractor(rowMapper);
                QueryCallable<List<T>> callable = new QueryCallable<List<T>>(shard, sql, args, extractor);

                callableList.add(callable);
            }

			@Override
            public List<T> processOperations() {
                List<List<T>> rawResultList = ShardJdbcTemplate.this.executeQuery(callableList);
                return AggregationUtil.aggregateObjectList(rawResultList);
            }
        };

        return processor;
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public List<Map<String, Object>> query(String sql, Object[] args, AggregationDescriptor descriptor) {
		Assert.notNull(sql, "sql must not be null");
		Assert.notNull(descriptor, "AggregationDescriptor must not be null");
		
        ShardOperationProcessor processor = null;

        if (descriptor.needGroupBy() || descriptor.needAggregation()) {
            processor = this.getAggregationProcessor(descriptor);
        } else {
            processor = this.getNonAggregationProcessor(new ColumnMapRowMapper());
        }

        List<Map<String, Object>> resultList = (List<Map<String, Object>>) this.process(sql, args, processor);
        AggregationUtil.sortIfNecessary(descriptor, resultList);

        return AggregationUtil.limitIfNecessary(descriptor, resultList);
    }


	@Override
	public <T> List<T> query(String sql, Object[] args, AggregatedRowMapper<T> rowMapper, AggregationDescriptor descriptor) {
		Assert.notNull(sql, "sql must not be null");
		Assert.notNull(rowMapper, "AggregatedRowMapper must not be null");
		Assert.notNull(descriptor, "AggregationDescriptor must not be null");
		
        List<Map<String, Object>> aggregatedRowList = this.query(sql, args, descriptor);
        List<T> resultList = new ArrayList<T>();

        for (Map<String, Object> aggregatedRow : aggregatedRowList) {
            T mappedRow = rowMapper.mapRow(aggregatedRow);
            resultList.add(mappedRow);
        }

        return resultList;
	}
    
	/**
	 * 在执行查询并从数据库获取结果集之前，会首先count结果集的行数
	 * 如果结果集行数超过ShardJdbcConfig中配置的maxRows，
	 * 会抛出SqlExecutionFailureException
	 * 
	 * @param callableList
	 * @return
	 */
	private <T> List<T> executeQuery(List<QueryCallable<T>> callableList) {
		if (this.getMaxRows() == 0) {
			return executor.execute(callableList);
		}

    	List<ShardAwareCallable<Integer>> countingCallableList = new ArrayList<ShardAwareCallable<Integer>>();
    	
    	for (QueryCallable<T> queryCallable : callableList) {
    		countingCallableList.add(new QueryCountingCallable(queryCallable));
    	}
    	
    	List<Integer> rowCountList = executor.execute(countingCallableList);
    	Integer rowCount = AggregationUtil.aggregateInteger(rowCountList);
    	
		if (rowCount > this.getMaxRows()) {
			StringBuilder builder = new StringBuilder();
			builder.append("result row count=").append(rowCount)
					.append(" exceeded limit=").append(this.getMaxRows())
					.append(" while executing sql=").append(callableList.iterator().next().sql);
			throw new SqlExecutionFailureException(builder.toString());
		} else {
			return executor.execute(callableList);
		}
    }
	
	@Override
	public void afterPropertiesSet() throws Exception {
        this.executor = new ShardAwareCallableExecutor(shardDataSource, this);	// setThreadPoolSize完成后才可进行executo的初始化
	}
	
	@Override
	public void destroy() throws Exception {
		if (executor != null) {
			executor.shutDown();
		}
	}

	/**
	 * 针对单个分表的查询操作的封装
	 */
    @SuppressWarnings("rawtypes")
    private class QueryCallable<T> implements ShardAwareCallable<T> {
        private Shard shard;
        private String sql;
        private Object[] args;
		private ResultSetExtractor extractor;

        private QueryCallable(Shard shard, String sql, Object[] args, ResultSetExtractor extractor) {
            this.shard = shard;
            this.sql = sql;
            this.args = args;
            this.extractor = extractor;
        }

        @SuppressWarnings("unchecked")
		@Override
        public T call() throws Exception {
        	JdbcTemplate jdbcTemplate = createJdbcTemplate(shard.getTargetDataSource(), ShardJdbcTemplate.this);
            String interceptedSql = shardDataSource.getSqlInterceptor().intercept(sql, shard.getTableContext());

            return (T) jdbcTemplate.query(interceptedSql, args, extractor);
        }

        @Override
        public Shard getShard() {
            return shard;
        }
    }

    /**
     * 针对单个分表的查询counting操作的封装
     */
    private class QueryCountingCallable implements ShardAwareCallable<Integer> {

        private Shard shard;
        private String sql;
        private Object[] args;

		public QueryCountingCallable(QueryCallable<?> queryCallable) {
			this.shard = queryCallable.shard;
			this.sql = queryCallable.sql;
			this.args = queryCallable.args;
		}

		@Override
		public Integer call() throws Exception {
			JdbcTemplate jdbcTemplate = createJdbcTemplate(shard.getTargetDataSource(), ShardJdbcTemplate.this);
	        String interceptedSql = shardDataSource.getSqlInterceptor().intercept(sql, shard.getTableContext());
	        StringBuilder countIt = new StringBuilder()
	        			.append("select count(*) from (")
	                    .append(interceptedSql)
	                    .append(") t");
	        Integer rowCount = jdbcTemplate.queryForInt(countIt.toString(), args);

	        return rowCount;
		}

		@Override
		public Shard getShard() {
			return shard;
		}

    }

    /**
     * 针对单个分表的更新操作的封装
     */
    private class UpdateCallable implements ShardAwareCallable<Integer> {
        private Shard shard;
        private String sql;
        private Object[] args;

        private UpdateCallable(Shard shard, String sql, Object[] args) {
            this.shard = shard;
            this.sql = sql;
            this.args = args;
        }

        @Override
        public Integer call() throws Exception {
        	JdbcTemplate jdbcTemplate = createJdbcTemplate(shard.getTargetDataSource(), ShardJdbcTemplate.this);
            String interceptedSql = shardDataSource.getSqlInterceptor().intercept(sql, shard.getTableContext());

            return jdbcTemplate.update(interceptedSql, args);
        }

        @Override
        public Shard getShard() {
            return shard;
        }
    }

    /**
     * 针对单个分表的批量更新操作的封装
     */
    private class BatchUpdateCallable implements ShardAwareCallable<int[]> {

        private Shard shard;
        private String sql;
        private List<Object[]> argsList;

        private BatchUpdateCallable(Shard shard, String sql, List<Object[]> argsList) {
            this.shard = shard;
            this.sql = sql;
            this.argsList = argsList;
        }

        @Override
        public Shard getShard() {
            return shard;
        }

        @Override
        public int[] call() throws Exception {
        	JdbcTemplate jdbcTemplate = createJdbcTemplate(shard.getTargetDataSource(), ShardJdbcTemplate.this);
            String interceptedSql = shardDataSource.getSqlInterceptor().intercept(sql, shard.getTableContext());
            
            return jdbcTemplate.batchUpdate(interceptedSql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int statementIndex) throws SQLException {
					Object[] args = argsList.get(statementIndex);
					
					for (int i = 0; i < args.length; i++) {
						Object arg = args[i];
						if (arg instanceof SqlParameterValue) {
							SqlParameterValue paramValue = (SqlParameterValue) arg;
							StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue, paramValue.getValue());
						}
						else {
							StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, arg);
						}
					}
				}
				
				@Override
				public int getBatchSize() {
					return argsList.size();
				}
			});
        }

    }

    /**
     * 分表查询处理器
     * addOperation()方法会被多次调用以添加一个分表的操作
     * 最终会调用processOperations()方法以获取对众分表操作的结果
     * @param <T>
     */
    private static interface ShardOperationProcessor<T> {

        public void addOperation(Shard shard, String sql, Object[] args);

        public T processOperations();

    }

}
