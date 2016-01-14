package com.sogou.bizdev.compass.core.proxyconnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class AbstractProxyConnectionTest extends TestCase
{
	static class Result
	{
	   Object[] args=null; 
	}
	
	private AbstractProxyConnection createProxyConnectionForPhysical(final int[] count,final String targetMethodName,final Result result,final Object expectValue)
	{
		return new AbstractProxyConnection() 
		{

			@Override
			protected String getTargetDataSourceNotFoundExceptionErrorMsg()
			{
				return null;
			}

			@Override
			protected DataSource getRandomDataSource() 
			{
				return null;
			}

			@Override
			protected DataSource getPhysicalDataSource() 
			{
				return null;
			}

			@Override
			protected Connection getOrCreatePhysicalConnection() throws SQLException 
			{
                count[0]++;
                return (Connection)Proxy.newProxyInstance(
                		ClassUtils.getDefaultClassLoader(), 
                		new Class<?>[]{Connection.class}, 
                		new InvocationHandler() 
                		{
							
							@Override
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
							{
								if(method.getName().equals(targetMethodName))
								{
									count[1]++;
									result.args=args;
									return expectValue;
								}
								return null;
							}
						});
                		
			}
		};
	}
	
	private AbstractProxyConnection createProxyConnectionForRandomAllowed(final int[] count,final String targetMethodName,final Object expectValue)
	{
		return new AbstractProxyConnection() 
		{

			@Override
			protected String getTargetDataSourceNotFoundExceptionErrorMsg()
			{
				return null;
			}

			@Override
			protected DataSource getRandomDataSource() 
			{
				return null;
			}

			@Override
			protected DataSource getPhysicalDataSource() 
			{
				return null;
			}

			@Override
			protected Connection getPhysicalOrRandomConnection() throws SQLException
			{
                count[0]++;
                return (Connection)Proxy.newProxyInstance(
                		ClassUtils.getDefaultClassLoader(), 
                		new Class<?>[]{Connection.class}, 
                		new InvocationHandler() 
                		{
							
							@Override
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
							{
								if(method.getName().equals(targetMethodName))
								{
									count[1]++;
									return expectValue;
								}
								return null;
							}
						});
                		
			}
		};
	}

	@Test
	public void testGetSchema() throws Exception
	{
		final int[] count=new int[2];
		String schema="mock";
		AbstractProxyConnection proxyConnection=this.createProxyConnectionForRandomAllowed(count, "getSchema",schema);
		assertTrue(proxyConnection.getSchema().equals(schema));
		assertTrue(count[0]==1 && count[1]==1);
		
		
	}
	
	@Test
	public void testAbort() throws Exception
	{
		final int[] count=new int[2];
		AbstractProxyConnection proxyConnection=this.createProxyConnectionForRandomAllowed(count, "abort",null);
		proxyConnection.abort(null);
		assertTrue(count[0]==1 && count[1]==1);
	}
	
	@Test
	public void testGetNetworkTimeout() throws Exception
	{
		final int[] count=new int[2];
		int timeout=5000;
		AbstractProxyConnection proxyConnection=this.createProxyConnectionForRandomAllowed(count, "getNetworkTimeout",timeout);
		assertTrue(proxyConnection.getNetworkTimeout()==timeout);
		assertTrue(count[0]==1 && count[1]==1);
	}
	
	@Test
	public void testSetSchema() throws Exception
	{
		final int[] count=new int[2];
		Result result=new Result();
		String schema="mock";
		AbstractProxyConnection proxyConnection=this.createProxyConnectionForPhysical(count, "setSchema",result,null);
		proxyConnection.setSchema(schema);
		assertTrue(count[0]==1 && count[1]==1);
		assertTrue(Arrays.equals(result.args, new Object[]{schema}));
	}
	
	@Test
	public void testSetNetworkTimeout() throws Exception
	{
		final int[] count=new int[2];
		Result result=new Result();
		ExecutorService executor=Executors.newCachedThreadPool();
		int milliseconds=5000;
		AbstractProxyConnection proxyConnection=this.createProxyConnectionForPhysical(count, "setNetworkTimeout",result,null);
		proxyConnection.setNetworkTimeout(executor, milliseconds);
		assertTrue(count[0]==1 && count[1]==1);
		assertTrue(Arrays.equals(result.args, new Object[]{executor, milliseconds}));
	}
	
}
