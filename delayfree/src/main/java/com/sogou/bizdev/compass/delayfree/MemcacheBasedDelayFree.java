package com.sogou.bizdev.compass.delayfree;

import org.springframework.util.ClassUtils;

import com.sogou.bizdev.compass.delayfree.cache.MyCache;

/**
 * 基于memcache的分布式延迟处理方案
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public class MemcacheBasedDelayFree extends AbstractDelayFree {

	private static final String KEY_PREFIX = ClassUtils.getPackageName(MemcacheBasedDelayFree.class)+"_";

	private MyCache mycache;

	@Override
	public Object getDelayKeyFromCache(String key) {
		return mycache.get(key);
	}

	@Override
	public void storeDelayKeyToCache(String key, Long period) {
		mycache.put(key, true, period.intValue());
	}

	@Override
	protected String buildCacheKey(Object delayKey) {
		// 直接用delayKey的字符串作为key后缀，不需要转换，如果是日期类型，需要自己转换key格式
		return KEY_PREFIX + delayKey;
	}

	public MyCache getMycache() {
		return mycache;
	}

	public void setMycache(MyCache mycache) {
		this.mycache = mycache;
	}

}
