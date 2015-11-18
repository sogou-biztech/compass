package com.sogou.bizdev.compass.core.selector.loadbalance;

/**
 * 
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Selectable 
{
	/**
	 * 用于带权类lb
	 * @return
	 */
	public int getWeight();
	
}
