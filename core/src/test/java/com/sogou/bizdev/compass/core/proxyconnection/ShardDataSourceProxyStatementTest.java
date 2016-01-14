package com.sogou.bizdev.compass.core.proxyconnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;

import org.junit.Test;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ShardDataSourceProxyStatementTest extends TestCase
{
    
	private Statement createStatement(final int[] count,final String targetMethodName,final Object expectValue)
	{
		return (Statement)Proxy.newProxyInstance(
				ClassUtils.getDefaultClassLoader(), 
				new Class<?>[]{Statement.class}, 
				new InvocationHandler() 
        		{
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
					{
						if(method.getName().equals(targetMethodName))
						{
							count[0]++;
							return expectValue;
						}
						return null;
					}
				});
		        
	}
	
	@Test
	public void testCloseOnCompletion() throws Exception
	{
		int[] count=new int[1];
		ShardDataSourceProxyStatement statement=new ShardDataSourceProxyStatement(
				this.createStatement(count, "closeOnCompletion", null),
				null);
		statement.closeOnCompletion();
		assertTrue(count[0]==1);
	}
	
	@Test
	public void testIsCloseOnCompletion() throws Exception
	{
		int[] count=new int[1];
		boolean result=true;
		ShardDataSourceProxyStatement statement=new ShardDataSourceProxyStatement(
				this.createStatement(count, "isCloseOnCompletion", result),
				null);
		assertTrue(statement.isCloseOnCompletion()==result);
		assertTrue(count[0]==1);
	}
}
