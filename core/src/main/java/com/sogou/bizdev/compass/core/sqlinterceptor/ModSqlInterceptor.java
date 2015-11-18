/**
 * 
 */
package com.sogou.bizdev.compass.core.sqlinterceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.exception.RouteKeyNotFoundException;
import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * @Description: 基于取模方式的sql替换拦截器
 * @author zjc
 * @since 1.0.0
 */
public class ModSqlInterceptor extends RewriteTableNameSqlInterceptor implements InitializingBean
{

	private static final String DEAFULT_PATTERN = "_%02d%02d";
	
	private String pattern = DEAFULT_PATTERN;
	
	@Override
	public String rewrite(String oldTableName, TableContext tableContext)
	{
		if(tableContext == null) 
		{
			throw new RouteKeyNotFoundException("TableContext is null");
		}
		Long dbId = Long.valueOf(tableContext.getDbIndex());
		Long tableId = Long.valueOf(tableContext.getTableIndex());
		String suffix = String.format(pattern, dbId, tableId);
		return oldTableName + suffix;
	}

	public String getPattern()
	{
		return pattern;
	}

	public void setPattern(String pattern)
	{
		if(!StringUtils.hasText(pattern))
		{
			throw new IllegalArgumentException("pattern is null!");
		}
		this.pattern = pattern.trim();
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		if(!StringUtils.hasText(this.pattern))
		{
			throw new IllegalArgumentException("pattern is null!");
		}
		
	}

	

}
