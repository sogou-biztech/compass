package com.sogou.bizdev.compass.aggregation.concurrency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.sogou.bizdev.compass.aggregation.datasource.Shard;
import com.sogou.bizdev.compass.aggregation.exception.ConcurrencyTimeoutException;
import com.sogou.bizdev.compass.aggregation.exception.SqlExecutionFailureException;
import com.sogou.bizdev.compass.aggregation.template.ShardJdbcConfig;
import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.router.TableContext;
import com.sogou.bizdev.compass.core.util.DefaultThreadFactory;

/**
 * 批量执行ShardAwareCallable的executor
 * @author yk
 * @since 1.0.0
 */
public class ShardAwareCallableExecutor 
{
	
	private static Logger logger = Logger.getLogger(ShardAwareCallableExecutor.class);  
	
	private ShardJdbcConfig config;
	private Map<String, ExecutorService> masterSlaveDataSourceToExecutorService;
	
	public ShardAwareCallableExecutor(ShardDataSource shardDataSource, ShardJdbcConfig config)
	{
		this.config = config;
		this.initExecutors(shardDataSource);
	}
	
	/**
	 * 初始化线程池
	 * 每个MasterSlaveDatasource对应一个线程池
	 * @param shardDataSource
	 */
	private void initExecutors(ShardDataSource shardDataSource)
	{
		this.masterSlaveDataSourceToExecutorService = new HashMap<String, ExecutorService>();
		
		for (String masterSlaveDataSourceId : shardDataSource.getMasterSlaveDataSourceIds())
		{
		
			int minPoolSize = Math.min(Runtime.getRuntime().availableProcessors(), this.config.getThreadPoolSize());
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(minPoolSize);
        	ThreadFactory threadFactory = new DefaultThreadFactory("compass-aggregation-" + masterSlaveDataSourceId);
        	RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        	
        	ExecutorService executor =  new ThreadPoolExecutor(
        										minPoolSize,
        										config.getThreadPoolSize(),
        										60, 
        										TimeUnit.SECONDS, 
        										workQueue, 
        										threadFactory, 
        										handler
        									);
        	
        	masterSlaveDataSourceToExecutorService.put(masterSlaveDataSourceId, executor);
		}
	}
	
	/**
	 * 销毁线程池
	 */
	public void shutDown() {
		for (ExecutorService executor : masterSlaveDataSourceToExecutorService.values()) {
			try {
				executor.shutdown();
			} catch (SecurityException e) {
				logger.error("security exception occurred while shutting down executor", e);
			}
		}
	}

    public <T> List<T> execute(List<? extends ShardAwareCallable<T>> callableList) {
        try {
        	ConcurrencyContext context = new ConcurrencyContext();
        	
        	List<Future<T>> futureList = this.submit(context, callableList);
        	List<T> resultList = this.getResult(context, futureList);

            return resultList;
        } catch (Throwable e) {
        	if (e instanceof SqlExecutionFailureException) {
        		throw (SqlExecutionFailureException) e;
        	} else {
        		throw new SqlExecutionFailureException(e);
        	}
        }
    }
    
    private ExecutorService getExecutorService(Shard shard)
    {
    	TableContext tableContext = shard.getTableContext();
    	String masterSlaveDataSourceId=tableContext.getMasterSlaveDataSourceId();
    	return masterSlaveDataSourceToExecutorService.get(masterSlaveDataSourceId);
    }
    
    private <T> List<Future<T>> submit(ConcurrencyContext context, List<? extends ShardAwareCallable<T>> callableList) throws Throwable {
    	List<Future<T>> futureList = new ArrayList<Future<T>>();
    	
		try {
			for (ShardAwareCallable<T> callable : callableList) {
				// 如果其他Callable执行时已发生异常
				// 那么抛出异常、停止继续向线程池提交Callable
				context.throwExceptionIfHasAny();

				CallableWrapper<T> wrapped = new CallableWrapper<T>(context, callable);
				ExecutorService executorService = this.getExecutorService(callable.getShard());
				Future<T> future = executorService.submit(wrapped);

				futureList.add(future);
			}

			return futureList;
		} catch (Throwable e) {
			// 当发生异常时，取消Future的执行
			for (Future<T> f : futureList) {
				f.cancel(true);
			}
			
			throw e;
		}
    }
    
    private <T> List<T> getResult(ConcurrencyContext context, List<Future<T>> futureList) throws Throwable {
        List<T> resultList = new ArrayList<T>();
        
        int index = 0;
        int size = futureList.size();

		try {
			for (; index < size; index++) {
				Future<T> f = futureList.get(index);
				// 如果查询过程中发生了异常
				// 那么直接将此异常抛出
				context.throwExceptionIfHasAny();

				try {
					T result = f.get(Integer.valueOf(config.getQueryTimeout()).longValue(), TimeUnit.SECONDS);
					resultList.add(result);
				} catch (TimeoutException e) {
					ConcurrencyTimeoutException wrappedException = new ConcurrencyTimeoutException(e);

					// 将异常设置到context中，以便其它还没有被执行的Callable感知到异常并取消数据库操作
					context.setException(wrappedException);
					throw wrappedException;
				}
			}

			// 如果查询过程中发生了异常
			context.throwExceptionIfHasAny();

			return resultList;
		} finally {
			// 如果遍历futureList过程中发生了异常
			// 那么取消剩下的Future的执行
			for (; index < size; index++) {
				Future<T> f = futureList.get(index);
				f.cancel(true);
			}
		}
	}

    private class CallableWrapper<T> implements Callable<T> {
    	
    	private ConcurrencyContext context;
    	private ShardAwareCallable<T> callable = null;
    	
    	public CallableWrapper(ConcurrencyContext context, ShardAwareCallable<T> callable) {
    		this.context = context;
    		this.callable = callable;
    	}

		@Override
		public T call() throws Exception {
			try {
				if (context.hasException()) {
					// 当其它线程中发生异常时
					// 或主线程从Future中取结果发生异常时
					// 不再执行此Callable的数据库操作了
					return null;
				}
				
				return callable.call();
			} catch (Exception e) {
				// 当此Callable中发生异常时，将异常放入ConcurrencyContext中
				// 其他Callable通过context检测到异常后，将不再执行数据库操作了
				context.setException(this.wrapException(e));

				return null;
			}
		}
		
		private SqlExecutionFailureException wrapException(Exception e) {
			if (e instanceof SqlExecutionFailureException) {
				return (SqlExecutionFailureException) e;
			} else {
				return new SqlExecutionFailureException(e);
			}
		}
    	
    }
    
}
