package com.sogou.bizdev.compass.delayfree;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>本地内存延迟处理方案</p>
 * <p>lru map存储，inserttion_ordered，当key超过map的capacity的的时候可能不准确</p>
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
public class LocalMemBasedDelayFree extends AbstractDelayFree{
	private static final String KEY_PREFIX = "com.sogou.bizdev.delayfree.store_";
	
	private static final int MAX_ENTRIES = 100*1000;

	private static final Map<String, Long> deleyKey2Time = new LinkedHashMap<String, Long>() {
		private static final long serialVersionUID = -2907847446501563897L;

		@Override
		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > MAX_ENTRIES;
		}
	};
	
	@Override
	public Object getDelayKeyFromCache(String key) {
		Long deadline = null;
		synchronized (deleyKey2Time) {
			deadline = deleyKey2Time.get(key);
		}
		if( null != deadline ){
			Long currentTime = System.currentTimeMillis();
			if( currentTime <= deadline ){
				return Boolean.TRUE;
			}else{
				return null;
			}
		}
		return null;
	}

	@Override
	public void storeDelayKeyToCache(String key, Long period) {
		Long deadline = System.currentTimeMillis() + period * 1000;
		synchronized (deleyKey2Time) {
			deleyKey2Time.put(key, deadline);
		}
	}

	@Override
	protected String buildCacheKey(Object delayKey) {
		// 直接用delayKey的字符串作为key后缀，不需要转换，如果是日期类型，需要自己转换key格式
		return KEY_PREFIX + delayKey;
	}

}
