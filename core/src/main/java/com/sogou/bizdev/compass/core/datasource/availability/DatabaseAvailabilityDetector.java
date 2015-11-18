package com.sogou.bizdev.compass.core.datasource.availability;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.sogou.bizdev.compass.core.datasource.DatabaseType;
import com.sogou.bizdev.compass.core.datasource.availability.DataSourceAvailableChangeListener.Status;
import com.sogou.bizdev.compass.core.datasource.availability.tester.DatabaseAliveTester;
import com.sogou.bizdev.compass.core.datasource.availability.tester.MysqlAliveTester;
import com.sogou.bizdev.compass.core.datasource.availability.tester.OracleAliveTester;
import com.sogou.bizdev.compass.core.util.DefaultThreadFactory;

/**
 * 数据库心跳探测器
 * 
 * @author gly
 * @since 1.0.0
 */
public class DatabaseAvailabilityDetector implements InitializingBean,DisposableBean
{
    private static Logger logger = LoggerFactory.getLogger(DatabaseAvailabilityDetector.class);  

	private static final int SCHEDULE_DELAY_TIME = 10;

	private static final int DEFAULT_INTERVAL = 15;
	
	private static final int DEFAULT_THREADPOOL_SIZE = 3; 
	
	/**
	 * 心跳探测间隔，默认为15s
	 */
	private int interval = DEFAULT_INTERVAL;   
	
	/**
	 * 心跳探测线程数，默认为3个，启动时从库个数而定
	 */
	private int detectPoolSize = DEFAULT_THREADPOOL_SIZE;   
	
	/**
	 * 是否开启心跳探测
	 */
	private boolean enabled = true;
	
	/**
	 * 是否开始探测
	 */
	private boolean started = false; 
	
	private static final Map<DatabaseType, DatabaseAliveTester> databaseAliveTesters = new HashMap<DatabaseType, DatabaseAliveTester>();
	
	static {
		databaseAliveTesters.put(DatabaseType.MYSQL, new MysqlAliveTester());
		databaseAliveTesters.put(DatabaseType.ORACLE, new OracleAliveTester());
	}

	private ScheduledExecutorService executorService =null;

	private Queue<DatabaseDetectingEvent> dataSourceDetectingEvents = new ConcurrentLinkedQueue<DatabaseDetectingEvent>();

	

	/**注册心跳探测监听事件
	 * @param event 心跳探测事件
	 * @param listener	心跳探测监听器
	 */
	public void registerDataSourceAvailableListener(final DatabaseDetectingEvent event, final DataSourceAvailableChangeListener listener)
	{
		dataSourceDetectingEvents.add(event);
		if(!this.enabled)
		{
			return;
		}
		
		Runnable runnable = new Runnable()
		{
			private Status currentStatus = Status.AVAILABLE;
			
			@Override
			public void run()
			{
				Status newStatus = null;
				if (logger.isDebugEnabled())
				{
					logger.debug("[Database Availability Detector] Ping data source "+event);
				}
				DatabaseType dbType = event.getDatabaseType();
				if (dbType == null)
				{
					logger.error("IGNORING database detecting due to Database Type is null for DatabaseDetectingEvent:"+event);
					return;
				}
				DatabaseAliveTester dbAliveTester = databaseAliveTesters.get(dbType);
				if (dbAliveTester==null)
				{
					logger.error("IGNORING database detecting due to Database Alive Tester for Database Type "+dbType+" is null for DatabaseDetectingEvent:"+event);
					return;
				}
				boolean alive = dbAliveTester.isDatabaseAlive(event);
				if (alive)
				{
					newStatus = Status.AVAILABLE;
				}
				else 
				{
					newStatus = Status.UNAVAILABLE;
					logger.warn("[Database Availability Detector] Exception occured during ping event "+event.toString());
				}
				if (currentStatus != newStatus)
				{
					listener.availableChanged(event.getDataSource().getId(), newStatus);
					if (logger.isWarnEnabled())
					{
						logger.warn("[Database Availability Detector] DataSourceId:["+event.getDataSource().getId()+"] is changed from "+currentStatus+" to "+newStatus);
					}
					currentStatus = newStatus;
				} 
				else 
				{
					if (logger.isDebugEnabled()) 
					{
						logger.debug("[Database Availability Detector] Status "+currentStatus+" not changed, event "+event);
					}
				}
			}
		};
		executorService.scheduleWithFixedDelay(runnable, SCHEDULE_DELAY_TIME, interval, TimeUnit.SECONDS);
		
	}
	
	public void unregisterDataSourceAvailableListener(DatabaseDetectingEvent event, DataSourceAvailableChangeListener listener) 
	{
		//TODO:要去掉定时器
		dataSourceDetectingEvents.remove(event);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public static final String ENABLED_PROPERTY_NAME="enabled";

	/**
	 * 用户可以不配置
	 * @param enabled
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isStarted() 
	{
		return this.started;
	}
	
	public int getInterval()
	{
		return interval;
	}
	
	public static final String INTERVAL_PROPERTY_NAME="interval";

	/**
	 * 用户可以不配置
	 * @param interval
	 */
	public void setInterval(int interval)
	{
		if(interval <=0 )
		{
			throw new IllegalArgumentException("interval:["+interval+"] must > 0!");
		}
		this.interval = interval;
	}

	public int getDetectPoolSize()
	{
		return detectPoolSize;
	}
	
	public static final String DETECT_POOL_SIZE_PROPERTY_NAME="detectPoolSize";

	/**
	 * 用户可以不配置
	 * @param detectPoolSize
	 */
	public void setDetectPoolSize(int detectPoolSize) 
	{
		if(detectPoolSize <=0 )
		{
			throw new IllegalArgumentException("detectPoolSize:["+detectPoolSize+"] must > 0!");
		}
		this.detectPoolSize = detectPoolSize;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[Datasource Availability Detector] started.");
		}
		started = true;
		executorService= new ScheduledThreadPoolExecutor(detectPoolSize,
				new DefaultThreadFactory("DatabaseAvailabilityDetector"));
	}
	
	@Override
	public void destroy() throws Exception {
		executorService.shutdown();
		started = false;
		if (logger.isDebugEnabled()) {
			logger.debug("[Datasource Availability Detector] stopped.");
		}
	}
	
}