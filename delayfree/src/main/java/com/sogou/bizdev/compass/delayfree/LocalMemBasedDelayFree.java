package com.sogou.bizdev.compass.delayfree;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.ClassUtils;

/**
 * <p>本地内存延迟处理方案</p>
 * <p>lru map存储，insertion_ordered，当key超过map的capacity的的时候可能不准确</p>
 *
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
public class LocalMemBasedDelayFree extends AbstractDelayFree {

    private static final String KEY_PREFIX = ClassUtils.getPackageName(LocalMemBasedDelayFree.class) + "_";

    private static final int MAX_ENTRIES = 100 * 1000;

    private static final Map<String, Long> DELAY_KEY_TO_TIME = new LinkedHashMap<String, Long>() {
        private static final long serialVersionUID = -2907847446501563897L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    @Override
    public Object getDelayKeyFromCache(String key) {
        Long deadline = null;
        synchronized (DELAY_KEY_TO_TIME) {
            deadline = DELAY_KEY_TO_TIME.get(key);
        }
        if (null != deadline) {
            Long currentTime = System.currentTimeMillis();
            if (currentTime <= deadline) {
                return Boolean.TRUE;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void storeDelayKeyToCache(String key, Long period) {
        Long deadline = System.currentTimeMillis() + period * 1000;
        synchronized (DELAY_KEY_TO_TIME) {
            DELAY_KEY_TO_TIME.put(key, deadline);
        }
    }

    @Override
    protected String buildCacheKey(Object delayKey) {
        // 直接用delayKey的字符串作为key后缀，不需要转换，如果是日期类型，需要自己转换key格式
        return KEY_PREFIX + delayKey;
    }

}
