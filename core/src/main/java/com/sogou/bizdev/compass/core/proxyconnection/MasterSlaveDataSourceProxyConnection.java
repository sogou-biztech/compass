package com.sogou.bizdev.compass.core.proxyconnection;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.util.ClassUtils;


import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.SingleDataSource;
import com.sogou.bizdev.compass.core.exception.NoConnectionAvailableException;


/**
 * 主从库代理Connection
 * 
 * @author gly
 * @since 1.0.0
 *
 */
public class MasterSlaveDataSourceProxyConnection extends AbstractProxyConnection 
{
	 
	
	private MasterSlaveDataSource  masterSlaveDataSource;
	
	public MasterSlaveDataSourceProxyConnection(MasterSlaveDataSource masterSlaveDataSource)
	{
		this.masterSlaveDataSource=masterSlaveDataSource;
	}
	
	public MasterSlaveDataSourceProxyConnection(MasterSlaveDataSource masterSlaveDataSource,String username, String password)
	{
		super(username,password);
		this.masterSlaveDataSource=masterSlaveDataSource;	
	}
	
	/** 
	 * 获取真实物理连接(可能为空)
	 */
	@Override
	protected SingleDataSource getPhysicalDataSource()
	{
 		return this.masterSlaveDataSource.select();
	}

	/** 
	 * 获取随机连接(默认选取主库)
	 */
	@Override
	protected SingleDataSource getRandomDataSource() 
	{
		return this.masterSlaveDataSource.getMasterDataSource();
	}
	
	
	
	@Override
	protected String getTargetDataSourceNotFoundExceptionErrorMsg()
	{
		
		return "caused by masterSlaveDataSource:["+this.masterSlaveDataSource.getId()+"],maybe masterSlave select config is lost!";
	}

	/** 
	 * 获取真实连接，有可能出错，此时需要快速将该库移除
	 * @see com.sogou.bizdev.compass.core.proxyconnection.AbstractProxyConnection#doGetConnection(javax.sql.DataSource)
	 */
	@Override
	protected Connection doGetConnection(DataSource dataSource) throws SQLException
	{
		if(dataSource == null)
		{
			throw new IllegalStateException("masterSlaveDataSource:["+ this.masterSlaveDataSource.getId()+"],doGetConnection dataSource is null!");
		}
		if(!(dataSource instanceof SingleDataSource))
		{
			throw new IllegalStateException("masterSlaveDataSource:["+ this.masterSlaveDataSource.getId()+"],dataSource Type:["+ClassUtils.getQualifiedName(dataSource.getClass())+"] must be SingleDataSource!");
		}
		
		SingleDataSource singleDataSource=(SingleDataSource)dataSource;
		
		try 
		{
			return super.doGetConnection(singleDataSource);
		}
		catch (SQLException e) 
		{
			logger.error("masterSlaveDataSource:["+ this.masterSlaveDataSource.getId()+"] get connection by databaseUrl:["+
					singleDataSource.getDatabaseUrl()+"] fail,will remove from availableDataSources",e);
			masterSlaveDataSource.removeUnavailableDataSource(singleDataSource.getId());
			throw e;
		}
		catch (Throwable e)
		{
		    logger.error("masterSlaveDataSource:["+ this.masterSlaveDataSource.getId()+"] get connection by databaseUrl:["+
		    		singleDataSource.getDatabaseUrl()+"] fail,will remove from availableDataSources",e);
			masterSlaveDataSource.removeUnavailableDataSource(singleDataSource.getId());
			throw new NoConnectionAvailableException(e);
		}
	}
	
	

}
