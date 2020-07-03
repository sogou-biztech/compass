package com.sogou.bizdev.compass.core.datasource.retriever;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class DatabasePropertiesRetrieverRegistry {
	
	private static Logger logger = LoggerFactory.getLogger(DatabasePropertiesRetrieverRegistry.class);
	
	/**
	 * 数据库配置反转解析器，基于C3P0连接池
	 */
	private static Map<Class<?>, DatabasePropertiesRetriever> databasePropertiesRetrievers = new HashMap<Class<?>, DatabasePropertiesRetriever>();
	
	static {
		Properties prop = new Properties();
		InputStream is=null;
		try {
			is= new ClassPathResource("/META-INF/retriever.properties").getInputStream();
			prop.load(is);
			Enumeration<Object> en = prop.keys();   
			while(en.hasMoreElements()){   
				String className = en.nextElement().toString();   
			    String databasePropertiesRetriever = prop.getProperty(className);
			    try {
		 			Class<?> clazz = Class.forName(className);
					databasePropertiesRetrievers
						.put(clazz, (DatabasePropertiesRetriever)Class.forName(databasePropertiesRetriever).newInstance());
				} catch (ClassNotFoundException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't load class "+className);
					}
				} catch (InstantiationException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't Instantiation class "+className+" DatabasePropertiesRetriever");
					}
				} catch (IllegalAccessException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't reflectively create an instance of class "+className+" DatabasePropertiesRetriever");
					}
				}    
			}   
			
		} catch (IOException e1) {
			throw new IllegalArgumentException(" can't load DataSource Pool's DatabasePropertiesRetriever from /META-INF/retriever.properties",e1);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(" can't close DataSource Pool's DatabasePropertiesRetriever from /META-INF/retriever.properties",e);
				} 
			}
		}
	}
	
	public static DatabasePropertiesRetriever get(Class<?> clazz){
		return databasePropertiesRetrievers.get(clazz);
	}
	
	
}
