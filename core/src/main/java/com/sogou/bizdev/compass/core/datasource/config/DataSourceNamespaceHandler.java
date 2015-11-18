package com.sogou.bizdev.compass.core.datasource.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Spring 命名空间处理类
 * 
 * @author gly
 * @since 1.0.0
 */
public class DataSourceNamespaceHandler extends NamespaceHandlerSupport 
{

	private static final String DATASOURCE_NAME = "datasource";
	
	@Override
	public void init() 
	{
		registerBeanDefinitionParser(DATASOURCE_NAME, new DatasourceBeanDefinitionParser());
	}

}
