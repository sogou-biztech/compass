package com.sogou.bizdev.compass.core.proxyconnection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import com.sogou.bizdev.compass.core.exception.TargetDataSourceNotFoundException;
import com.sogou.bizdev.compass.core.util.JdbcMethodUtils;

/**
 * @Description: 代理connection，执行sql 替换
 * @author zjc
 * @since 1.0.0
 *
 */
public abstract class AbstractProxyConnection implements Connection
{
	protected static Logger logger = Logger.getLogger(MasterSlaveDataSourceProxyConnection.class); 
	
	private Connection physicalConnection;
	
	private String username;
	private String password;
	/**
	 * 标识当前是否使用了随机的连接
	 */
	private boolean useRandomConnection=false;

	public AbstractProxyConnection()
	{
		
	}
	
	public AbstractProxyConnection(String username, String password)
	{
 		this.username=username;
 		this.password=password;
	}
	
	
	/**获取实际连接
	 * @return
	 */
	protected abstract DataSource getPhysicalDataSource();
	
	/**获取随机连接
	 * @return
	 */
	protected abstract DataSource getRandomDataSource();
	
	protected abstract String getTargetDataSourceNotFoundExceptionErrorMsg();
	
	/**
	 * 获取实际或者随机的数据库连接
	 * 当hibernate类似初始化时，可能产生useRandomConnection=true的情况
	 * @return
	 * @throws SQLException
	 */
	protected Connection getPhysicalOrRandomConnection() throws SQLException
	{
		Connection physicalConnectionToUse=this.doGetOrCreatePhysicalConnection();
		if(physicalConnectionToUse!=null)
		{
			return physicalConnectionToUse;
		}
		
		Connection randomConnection=this.createRandomConnection();
		if(randomConnection!=null)
		{
			this.physicalConnection=randomConnection;
			this.useRandomConnection=true;
			return randomConnection;
		}
		
		throw new TargetDataSourceNotFoundException("getPhysicalOrRandomConnection error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());	
	}
	
	/**获取当前的实际连接，可能产生为空的情况
	 * @return
	 * @throws SQLException
	 */
	protected Connection getOrCreatePhysicalConnection() throws SQLException 
	{
		Connection connection=this.doGetOrCreatePhysicalConnection();
		if(connection==null)
		{
			throw new TargetDataSourceNotFoundException("getOrCreatePhysicalConnection error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		
		return connection;
	}
	
	/**
	 * 创建随机连接
	 * @return
	 * @throws SQLException
	 */
	protected Connection createRandomConnection() throws SQLException
	{
		return this.doGetConnection(this.getRandomDataSource());
	}
	
	/**
	 * 获取实际连接
	 * @return
	 * @throws SQLException
	 */
	protected Connection doGetOrCreatePhysicalConnection() throws SQLException 
	{
		if(this.physicalConnection!=null)
		{
			return this.physicalConnection;
		}
		
		return this.physicalConnection=this.doGetConnection(this.getPhysicalDataSource());
		
	}
	
	protected Connection doGetConnection(DataSource dataSource) throws SQLException
	{
		if(dataSource==null)
		{
			return null;
		}
		if(this.username!=null)
		{
			return dataSource.getConnection(this.username,this.password);
		}
		else
		{
			return dataSource.getConnection();
		}

	}
	
 
	//-----不可以在随机的连接上进行的操作-----------------------------
	
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareCall error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareCall(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setAutoCommit error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("commit error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().commit();
	}

	@Override
	public void rollback() throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("rollback error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().rollback();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setTransactionIsolation error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().setTransactionIsolation(level);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareCall error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setSavepoint error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setSavepoint error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("rollback error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("releaseSavepoint error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		this.getOrCreatePhysicalConnection().releaseSavepoint(savepoint);
	}
	
	@Override
	public Statement createStatement() throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().createStatement();
	}

	@Override
	public Statement createStatement(int resultSetType,int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	
	
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return  this.getOrCreatePhysicalConnection().createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareCall error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareCall(sql, resultSetType, resultSetConcurrency,resultSetHoldability);
	}
	
	
	
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		PreparedStatement ps = this.getOrCreatePhysicalConnection().prepareStatement(sql);
		return ps;
	}
	
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,int resultSetConcurrency) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
	}
	
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,int resultSetConcurrency, int resultSetHoldability) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareStatement(sql, resultSetType, resultSetConcurrency,resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("prepareStatement error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return this.getOrCreatePhysicalConnection().prepareStatement(sql, columnNames);
	}
	
	
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setReadOnly error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		getOrCreatePhysicalConnection().setReadOnly(readOnly);
	}
	
	@Override
	public void setCatalog(String catalog) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setCatalog error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		getOrCreatePhysicalConnection().setCatalog(catalog);
	}
	
	
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setTypeMap error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		getOrCreatePhysicalConnection().setTypeMap(map);
	}
	
	
	@Override
	public void setHoldability(int holdability) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setHoldability error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		getOrCreatePhysicalConnection().setHoldability(holdability);
	}
	
	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setClientInfo error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		try
		{
			getOrCreatePhysicalConnection().setClientInfo(name, value);
		}
		catch(SQLClientInfoException ex)
		{
			throw ex;
		}
		catch (SQLException ex )
		{
			throw new SQLClientInfoException(null,ex);
		}
	}
	
    @Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setClientInfo error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		try
		{
			getOrCreatePhysicalConnection().setClientInfo(properties);
		}
		catch(SQLClientInfoException ex)
		{
			throw ex;
		}
		catch (SQLException ex )
		{
			throw new SQLClientInfoException(null,ex);
		}

	}
    
    @Override
    public Clob createClob() throws SQLException
	{
    	if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createClob error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createClob();
	}

    @Override
	public Blob createBlob() throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createBlob error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createNClob error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createSQLXML error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createSQLXML();
	}
	
	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createArrayOf error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("createStruct error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		return getOrCreatePhysicalConnection().createStruct(typeName, attributes);
	}
	
	
	//---------------------------------------------------------------------
	// Implementation of JDBC 4.1's method
	//---------------------------------------------------------------------
	//@Override
	public void setSchema(String schema) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setSchema error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}		
		JdbcMethodUtils.invokeJdbcMethod(Connection.class,"setSchema",new Class<?>[]{String.class},getOrCreatePhysicalConnection(),new Object[]{schema});
	}

	//@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException 
	{
		if(this.useRandomConnection)
		{
			throw new TargetDataSourceNotFoundException("setNetworkTimeout error,"+this.getTargetDataSourceNotFoundExceptionErrorMsg());
		}
		JdbcMethodUtils.invokeJdbcMethod(Connection.class,"setNetworkTimeout",new Class<?>[]{Executor.class,int.class},getOrCreatePhysicalConnection(),new Object[]{executor,milliseconds});
		
	}

	
	
	//-----------------------可以在随机的连接上进行的操作-----------------
	
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException 
	{
		return getPhysicalOrRandomConnection().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return getPhysicalOrRandomConnection().isWrapperFor(iface);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException 
	{
		return getPhysicalOrRandomConnection().nativeSQL(sql);
	}

	@Override
	public boolean getAutoCommit() throws SQLException 
	{
		return getPhysicalOrRandomConnection().getAutoCommit();
	}


	@Override
	public DatabaseMetaData getMetaData() throws SQLException 
	{
		return this.getPhysicalOrRandomConnection().getMetaData();
	}
	
	@Override
	public void close() throws SQLException 
	{
		getPhysicalOrRandomConnection().close();
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		return getPhysicalOrRandomConnection().isClosed();
	}

	

	@Override
	public boolean isReadOnly() throws SQLException 
	{
		return getPhysicalOrRandomConnection().isReadOnly();
	}
	

	@Override
	public String getCatalog() throws SQLException 
	{
		return getPhysicalOrRandomConnection().getCatalog();
	}

	@Override
	public int getTransactionIsolation() throws SQLException 
	{
		return getPhysicalOrRandomConnection().getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		return getPhysicalOrRandomConnection().getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException 
	{
		getPhysicalOrRandomConnection().clearWarnings();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException
	{
		return getPhysicalOrRandomConnection().getTypeMap();
	}

	
	@Override
	public int getHoldability() throws SQLException
	{
		return getPhysicalOrRandomConnection().getHoldability();
	}


	@Override
	public boolean isValid(int timeout) throws SQLException
	{
		return getPhysicalOrRandomConnection().isValid(timeout);
	}

	@Override
	public String getClientInfo(String name) throws SQLException 
	{
		return getPhysicalOrRandomConnection().getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException
	{
		return getPhysicalOrRandomConnection().getClientInfo();
	}

	//---------------------------------------------------------------------
	// Implementation of JDBC 4.1's method
	//---------------------------------------------------------------------
	//@Override
	public String getSchema() throws SQLException 
	{
		return (String)JdbcMethodUtils.invokeJdbcMethod(Connection.class,"getSchema",new Class<?>[0],getPhysicalOrRandomConnection(),new Object[0]);
	}
	//@Override
	public void abort(Executor executor) throws SQLException 
	{
		JdbcMethodUtils.invokeJdbcMethod(Connection.class,"abort",new Class<?>[]{Executor.class},getPhysicalOrRandomConnection(),new Object[]{executor});	
	}
	//@Override
	public int getNetworkTimeout() throws SQLException 
	{
		return (Integer)JdbcMethodUtils.invokeJdbcMethod(Connection.class,"getNetworkTimeout",new Class<?>[0],getPhysicalOrRandomConnection(),new Class<?>[0]);
	}
   
}
