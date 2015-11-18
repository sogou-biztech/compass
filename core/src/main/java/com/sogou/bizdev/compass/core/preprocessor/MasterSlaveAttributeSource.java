/**
 * 
 */
package com.sogou.bizdev.compass.core.preprocessor;

import java.lang.reflect.Method;

/** 
 * @Description: 主从属性源,根据方法判断当前方法是去主库还是从库
 * @author zhangjuncheng@sogou-inc.com
 * @date 2014-5-7 下午4:35:39 
 * @since 1.0.0 
 */
public interface MasterSlaveAttributeSource {

	/**
	 * Return the MASTER/SLAVE attribute for this method.
	 * Return null if the method is no master/slave matcher.
	 * @param method method
	 * @param targetClass target class. May be <code>null</code>, in which
	 * case the declaring class of the method must be used.
	 * @return MasterSlaveAttribute the matching MASTER/SLAVE attribute,
	 * or <code>null</code> if none found
	 */
	MasterSlaveAttribute getMasterSlaveAttribute(Method method, Class<?> targetClass);
	
}
