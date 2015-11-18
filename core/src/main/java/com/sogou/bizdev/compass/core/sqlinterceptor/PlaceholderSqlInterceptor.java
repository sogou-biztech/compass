/**
 * 
 */
package com.sogou.bizdev.compass.core.sqlinterceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * @Description: 基于占位符的sql替换拦截器
 * @author zjc
 * @since 1.0.0
 */
public class PlaceholderSqlInterceptor implements SqlInterceptor,InitializingBean 
{

	private static final String DEAFULT_PATTERN = "_%02d%02d";
	
	private String pattern = DEAFULT_PATTERN;
	
	private String placeholder;

	public String getPlaceholder() 
	{
		return placeholder;
	}

	public void setPlaceholder(String placeholder) 
	{
		if(!StringUtils.hasText(placeholder))
		{
			throw new IllegalArgumentException("placeholder is null!");
		}
		this.placeholder = placeholder.trim();
	}

	@Override
	public String intercept(String sql, TableContext tableContext) 
	{
		if (!StringUtils.hasText(sql))
		{
			return sql;
		}
		Long dbId = Long.valueOf(tableContext.getDbIndex());
		Long tableId = Long.valueOf(tableContext.getTableIndex());
		String suffix = String.format(pattern, dbId, tableId);
		return sql.replace(placeholder, suffix);
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
		if (!StringUtils.hasText(this.placeholder)) 
		{
			throw new IllegalArgumentException("placeholder is null!");
		}
		
		if(!StringUtils.hasText(this.pattern))
		{
			throw new IllegalArgumentException("pattern is null!");
		}
	}

}
