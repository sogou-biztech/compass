/**
 * 
 */
package com.sogou.bizdev.compass.core.sqlinterceptor;

import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * @Description: sql替换接口,与ShardDataSource一一对应
 * @author zjc
 * @since 1.0.0
 */
public interface SqlInterceptor 
{
	/**
	 * 对输入sql进行替换，输出新的sql
	 * @param sql
	 * @return
	 */
	public String intercept(String sql, TableContext tc);
}
