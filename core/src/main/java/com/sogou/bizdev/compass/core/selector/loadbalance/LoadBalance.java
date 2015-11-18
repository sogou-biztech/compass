package com.sogou.bizdev.compass.core.selector.loadbalance;

import java.util.List;

/**
 * 负载均衡器
 * 
 * @author xr
 * @since 1.0.0
 */
public interface LoadBalance 
{
	/**
	 * 
	 * @param <T>
	 * @param selectables
	 * @return
	 */
	public <T extends Selectable> T select(List<T> selectables);
}
