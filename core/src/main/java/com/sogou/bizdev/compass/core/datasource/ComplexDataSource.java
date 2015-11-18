package com.sogou.bizdev.compass.core.datasource;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.util.Assert;

public abstract class ComplexDataSource extends AbstractDataSource
{
	private PrintWriter logWriter;

	private int loginTimeout;

	
    @Override
	public PrintWriter getLogWriter()
    {
		return logWriter;
	}

    @Override
	public void setLogWriter(PrintWriter logWriter) 
    {
		this.logWriter = logWriter;
	}

	@Override
	public int getLoginTimeout() {
		return loginTimeout;
	}

	@Override
	public void setLoginTimeout(int loginTimeout) {
		this.loginTimeout = loginTimeout;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		Assert.notNull(iface, "Interface argument must not be null");
		if (!(DataSource.class.equals(iface))) {
			throw new SQLException(
					"DataSource of type ["
							+ super.getClass().getName()
							+ "] can only be unwrapped as [javax.sql.DataSource], not as ["
							+ iface.getName());
		}
		return (T) this;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException 
	{
		return DataSource.class.equals(iface);

	}
	
	
}
