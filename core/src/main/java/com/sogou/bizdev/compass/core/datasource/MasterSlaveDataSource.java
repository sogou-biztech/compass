package com.sogou.bizdev.compass.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizdev.compass.core.datasource.availability.DataSourceAvailableChangeListener;
import com.sogou.bizdev.compass.core.datasource.availability.DatabaseAvailabilityDetector;
import com.sogou.bizdev.compass.core.datasource.availability.DatabaseDetectingEvent;
import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;
import com.sogou.bizdev.compass.core.proxyconnection.MasterSlaveDataSourceProxyConnection;
import com.sogou.bizdev.compass.core.selector.MasterSlaveDataSourceSelector;
import com.sogou.bizdev.compass.core.selector.loadbalance.LoadBalance;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 主从数据源
 * 
 * @author gly
 * @since 1.0.0
 */
public class MasterSlaveDataSource extends AbstractDataSource implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(MasterSlaveDataSource.class);  

	/**
	 * 所属的分库数据源id(shardDataSource)
	 */
	private String shardDataSourceId;

	/**
	 * 主库数据源id(this.getId()+.master)
	 */
	private SingleDataSource masterDataSource;


	/**
	 * 从库数据源
	 */
	private Map<String,SingleDataSource> idToSlaveDataSource=Collections.<String,SingleDataSource>emptyMap();
	
	/**
	 * 从库的ids数组，与用户配置的顺序一致
	 */
	private List<String> slaveDataSourceIds=Collections.<String>emptyList();

	/**
	 * 存活的从库数据源
	 */
	private ConcurrentHashMap<String,SingleDataSource> aliveIdToSlaveDataSource = new ConcurrentHashMap<String,SingleDataSource>();

	/**
	 * 数据库心跳探测器,可以为空
	 */
	private DatabaseAvailabilityDetector databaseAvailabilityDetector = null; 

	/**
	 * 心跳探测SQL
	 */
	private String pingStatement=null;

	/**
	 * 主从选择器(含读写选择+负载均衡)
	 */
	private final MasterSlaveDataSourceSelector masterSlaveDataSourceSelector=new MasterSlaveDataSourceSelector(this);
	
	

	/**
	 * 主从数据源初始化，开启心跳探测监听器,
	 * 在DatasourceBeanDefinitionParser里面设置为Bean的初始化方法
	 */
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		if(this.masterDataSource==null)
		{
			throw new IllegalArgumentException("masterDataSource is null!");
		}
		
		if(!StringUtils.hasText(this.getId()))
		{
			throw new IllegalArgumentException("masterSlaveDataSourceId is null!");
		}
		
		if(!StringUtils.hasText(this.shardDataSourceId))
		{
			throw new IllegalArgumentException("shardDataSourceId is null!");
		}
		
		for (Entry<String, SingleDataSource> entry : this.idToSlaveDataSource.entrySet())
		{
			String slaveDataSourceId = entry.getKey();
			SingleDataSource slaveDataSource =entry.getValue();
			aliveIdToSlaveDataSource.put(slaveDataSourceId, slaveDataSource);
		}
		this.masterSlaveDataSourceSelector.setAliveSlaves(aliveIdToSlaveDataSource);

		if(this.databaseAvailabilityDetector == null)
		{
			return;
		}
		
		if(!StringUtils.hasText(pingStatement))
		{
			throw new IllegalArgumentException("pingStatement is null!");
		}
		
		//注册从库心跳探测事件
		for(Entry<String, SingleDataSource> entry : idToSlaveDataSource.entrySet())
		{
			SingleDataSource slaveDataSource =entry.getValue();

	        DatabaseDetectingEvent slaveEvent = new DatabaseDetectingEvent(
					slaveDataSource, 
					slaveDataSource.getDatabaseType(),
					pingStatement);
			DataSourceAvailableChangeListener slaveListener = new DataSourceAvailableChangeListener() 
			{
                @Override
				public void availableChanged(String dataSourceId, Status status) 
				{
					SingleDataSource dataSource = idToSlaveDataSource.get(dataSourceId);
					if(dataSource==null)
					{
						logger.error("Invalid slave dataSource id:["+dataSourceId+"] of MasterSlaveDataSource:["+getId()+"]");
						return;
					}

					switch(status)
					{
					case AVAILABLE:
						aliveIdToSlaveDataSource.put(dataSourceId,dataSource);
						if(logger.isDebugEnabled())
						{
							logger.debug(" alivedSlaveDataSources add datasource,dataSourceId = "+dataSourceId+",status="+DataSourceAvailableChangeListener.Status.AVAILABLE);
						}
						break;
					case UNAVAILABLE:
						aliveIdToSlaveDataSource.remove(dataSourceId);
						if(logger.isDebugEnabled())
						{
							logger.debug(" alivedSlaveDataSources remove datasource,dataSourceId = "+dataSourceId+",status="+DataSourceAvailableChangeListener.Status.UNAVAILABLE);
						}
						break;
					}
					//检测到从库数据源变化后，需要通知主从选择策略器重新初始化
					masterSlaveDataSourceSelector.setAliveSlaves(aliveIdToSlaveDataSource);

				}

			};
			databaseAvailabilityDetector.registerDataSourceAvailableListener(slaveEvent, slaveListener);
		}

	}

	public void removeUnavailableDataSource(String unavailableDataSourceId)
	{
		SingleDataSource oldSlaveDataSource=this.aliveIdToSlaveDataSource.remove(unavailableDataSourceId);
		
		if(oldSlaveDataSource!=null)
		{
			if(logger.isWarnEnabled())
			{
				logger.warn("detected connnection fail,alivedSlaveDataSources remove datasource,dataSourceId:["+unavailableDataSourceId+"]");
			}
		}

		masterSlaveDataSourceSelector.setAliveSlaves(this.aliveIdToSlaveDataSource);


	}

	/**
	 * 获取代理数据库连接
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException 
	{
		Connection connection= new MasterSlaveDataSourceProxyConnection(this);
		if(logger.isDebugEnabled())
		{
			logger.debug("get MasterSlaveProxyConnection from MasterSlaveDataSource:["+this.getId()+"]");
		}
		return connection;
	}

	/**
	 * 获取代理数据库连接(用户名,密码)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException 
	{
		Connection connection= new MasterSlaveDataSourceProxyConnection(this,username,password);
		if(logger.isDebugEnabled())
		{
			logger.debug("get MasterSlaveProxyConnection from MasterSlaveDataSource:["+this.getId()+"],username:["+username+"],password:["+password+"]");
		}
		return connection;
	}

	/**
	 * 根据主从选择选择一个真实库
	 * @return
	 */
	public SingleDataSource select()
	{
		SingleDataSource dataSource= masterSlaveDataSourceSelector.select();
		if(logger.isDebugEnabled())
		{
			logger.debug("complete select from MasterSlaveDataSource:["+this.getId()+"],shardDataSourceId:["+shardDataSourceId+"]");
		}
		return dataSource;
	}

	/**
	 * 获取当前主从库的线程池统计信息
	 * @return
	 */
	public List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics()
	{
		List<DataSourceConnectionStatistic>  connectionStatistics=new ArrayList<DataSourceConnectionStatistic>();
	    connectionStatistics.addAll(this.masterDataSource.getDataSourceConnectionStatistics());
		if(!CollectionUtils.isEmpty(this.idToSlaveDataSource))
		{
			for (Map.Entry<String,SingleDataSource> entry : this.idToSlaveDataSource.entrySet())
			{ 
				SingleDataSource slaveDataSource = entry.getValue(); 
				connectionStatistics.addAll(slaveDataSource.getDataSourceConnectionStatistics());
			} 
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("get DataSource Connection Statistics from MasterSlaveDataSource:["+this.getId()+"],return "+connectionStatistics);
		}
		return connectionStatistics;
	}

	public String getShardDataSourceId() 
	{
		return shardDataSourceId;
	}
	
	public static final String SHARD_DATA_SOURCE_ID_PROPERTY_NAME="shardDataSourceId";
	/**
	 * 必须要设置
	 * @param shardDataSourceId
	 */
	public void setShardDataSourceId(String shardDataSourceId) 
	{
		if(!StringUtils.hasText(shardDataSourceId))
		{
			throw new IllegalArgumentException("shardDataSourceId is null!");
		}
		this.shardDataSourceId = shardDataSourceId.trim();
	}



	public SingleDataSource getMasterDataSource()
	{
		return masterDataSource;
	}

	public static final String MASTER_DATA_SOURCE_PROPERTY_NAME="masterDataSource";
	/**
	 * 必须要设置
	 * @param masterDataSource
	 */
	public void setMasterDataSource(SingleDataSource masterDataSource) 
	{
		if(masterDataSource==null)
		{
			throw new IllegalArgumentException("masterDataSource is null!");
		}
		this.masterDataSource = masterDataSource;
	}

	public Map<String,SingleDataSource> getIdToSlaveDataSource()
	{
		return idToSlaveDataSource;
	}
	
	/**
	 * 返回的列表顺序与用户配置的顺序一致
	 * @return
	 */
	public List<String> getSlaveDataSourceIds() 
	{
		return slaveDataSourceIds;
	}
	
	public static final String SLAVE_DATA_SOURCES_PROPERTY_NAME="slaveDataSources";
    /**
     * 用户可以不设置
     * @param slaveDataSources 参数不能为null
     */
	public void setSlaveDataSources(List<SingleDataSource> slaveDataSources)
	{
		if(slaveDataSources==null)
		{
			throw new IllegalArgumentException("slaveDataSources is null!");
		}
		
		this.idToSlaveDataSource=new HashMap<String, SingleDataSource>(slaveDataSources.size());
		this.slaveDataSourceIds=new ArrayList<String>(slaveDataSources.size());
		for(SingleDataSource slaveDataSource : slaveDataSources)
		{
			String slaveDataSourceId=slaveDataSource.getId();
			if(!StringUtils.hasText(slaveDataSourceId))
			{
				throw new IllegalArgumentException("masterSlaveDataSourceId:["+this.getId()+"] has slaveDataSourceId null!");
			}
			
			if(this.idToSlaveDataSource.containsKey(slaveDataSourceId))
			{
				throw new IllegalArgumentException("masterSlaveDataSourceId:["+this.getId()+"],slaveDataSourceId:["+slaveDataSourceId+"] repeat!");
			}
			
			this.idToSlaveDataSource.put(slaveDataSourceId, slaveDataSource);
			this.slaveDataSourceIds.add(slaveDataSourceId);
		}
	}

	

	public DatabaseAvailabilityDetector getDatabaseAvailabilityDetector()
	{
		return databaseAvailabilityDetector;
	}

	public static final String AVAILABILITY_DETECTOR_PROPERTY_NAME="databaseAvailabilityDetector";
	/**
	 * 用户可以不设置
	 * @param availabilityDetector
	 */
	public void setDatabaseAvailabilityDetector(DatabaseAvailabilityDetector availabilityDetector)
	{
		if(availabilityDetector==null)
		{
			throw new IllegalArgumentException("availabilityDetector is null!");
		}
		this.databaseAvailabilityDetector = availabilityDetector;
	}

	public String getPingStatement()
	{
		return pingStatement;
	}

	public static final String PING_STATEMENT_PROPERTY_NAME="pingStatement";
	/**
	 * 用户可以不设置
	 * @param pingStatement
	 */
	public void setPingStatement(String pingStatement)
	{
		if(!StringUtils.hasText(pingStatement))
		{
			throw new IllegalArgumentException("pingStatement is null!");
		}
		this.pingStatement = pingStatement.trim();
	}
	
	public static final String LOAD_BALANCE_PROPERTY_NAME="loadBalance";
	
	/**
	 * 用户可以不设置,默认随机
	 * @param loadBalance
	 */
	public void setLoadBalance(LoadBalance loadBalance)
	{
		if(loadBalance==null)
		{
			throw new IllegalArgumentException("loadBalance is null!");
		}
		this.masterSlaveDataSourceSelector.setLoadBalance(loadBalance);
	}

	@Override
	public String toString() {
		return "MasterSlaveDataSource [id=" + this.getId()
		+ ", masterDataSource=" + masterDataSource
		+ ", slaveDataSources=" + idToSlaveDataSource
		+ ", alivedSlaveDataSources=" + aliveIdToSlaveDataSource
		+ ", availabilityDetector=" + databaseAvailabilityDetector
		+ ", pingStatement=" + pingStatement
		+ ", masterSlaveDataSourceSelector="
		+ masterSlaveDataSourceSelector+ "]";
	}



}
