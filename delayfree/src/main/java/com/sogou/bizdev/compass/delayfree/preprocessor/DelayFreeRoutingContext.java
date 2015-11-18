/**
 * 
 */
package com.sogou.bizdev.compass.delayfree.preprocessor;

import java.lang.reflect.Method;

import com.sogou.bizdev.compass.core.preprocessor.RouteContext;

/**
 * @author zjc
 * @since 1.0.0
 */
public class DelayFreeRoutingContext extends RouteContext {

	/**
	 * 反延时标记
	 */
	private Object antiDelayKey;

	public DelayFreeRoutingContext(Method method, Object routeKey,
			boolean masterMode, Object antiDelayKey) {
		super(method, routeKey, masterMode);
		this.antiDelayKey = antiDelayKey;
	}

	public Object getAntiDelayKey() {
		return antiDelayKey;
	}

	@Override
	public String toString() {
		return String.format("DelayFreeRoutingContext [method=%s, routingKey=%s, masterMode=%s, antiDelayKey=%s]",
				getMethod(), getRouteKey(), isMasterMode(), antiDelayKey);
	}
	
	

}
