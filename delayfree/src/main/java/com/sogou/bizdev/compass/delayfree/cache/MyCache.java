package com.sogou.bizdev.compass.delayfree.cache;


/**
 * 
 * @author wl
 *
 */
public interface MyCache {

	public void put(String key, Object o);

	public void put(String key, Object o, Integer timeToLiveSeconds);

	public Object get(String key);

	public boolean delete(String key);
}
