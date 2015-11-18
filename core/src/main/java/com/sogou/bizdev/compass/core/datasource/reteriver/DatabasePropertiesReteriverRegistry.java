package com.sogou.bizdev.compass.core.datasource.reteriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class DatabasePropertiesReteriverRegistry {
	
	private static Logger logger = LoggerFactory.getLogger(DatabasePropertiesReteriverRegistry.class);  
	
	/**
	 * 数据库配置反转解析器，基于C3P0连接池
	 */
	private static Map<Class<?>,DatabasePropertiesReteriver> databasePropertiesReterivers = new HashMap<Class<?>,DatabasePropertiesReteriver>();
	
	static {
		Properties prop = new Properties();
		InputStream is=null;
		try {
			is= new ClassPathResource("/META-INF/reteriver.properties").getInputStream();   
			prop.load(is);
			Enumeration<Object> en = prop.keys();   
			while(en.hasMoreElements()){   
				String className = en.nextElement().toString();   
			    String databasePropertiesReteriver = prop.getProperty(className);   
			    try {
		 			Class<?> clazz = Class.forName(className);
					databasePropertiesReterivers.put(clazz, (DatabasePropertiesReteriver)Class.forName(databasePropertiesReteriver).newInstance());
				} catch (ClassNotFoundException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't load class "+className);
					}
				} catch (InstantiationException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't Instantiation class "+className+" DatabasePropertiesReteriver");
					}
				} catch (IllegalAccessException e) {
					if(logger.isDebugEnabled()){
						logger.debug("can't reflectively create an instance of class "+className+" DatabasePropertiesReteriver");
					}
				}    
			}   
			
		} catch (IOException e1) {
			throw new IllegalArgumentException(" can't load DataSource Pool's DatabasePropertiesReteriver from /META-INF/reteriver.properties",e1);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(" can't close DataSource Pool's DatabasePropertiesReteriver from /META-INF/reteriver.properties",e);
				} 
			}
		}
	}
	
	public static DatabasePropertiesReteriver get(Class<?> clazz){
		return databasePropertiesReterivers.get(clazz);
	}
	
	
}
