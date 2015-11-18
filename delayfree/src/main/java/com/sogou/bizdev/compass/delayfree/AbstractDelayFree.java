package com.sogou.bizdev.compass.delayfree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 延迟处理方案抽象基类，存储方式可扩展
 * 
 * @author xr
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractDelayFree implements DelayFree {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final Long DEFAULT_DELAY_SECONDS = 10L;

	/**
	 * 多长时间范围内需要做延迟处理
	 */
	private Long period;

	public Long getPeriod() {
		if (null == period) {
			return DEFAULT_DELAY_SECONDS;
		}
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	@Override
	public boolean isInDelay(Object delayKey) {
		if (null == delayKey) {
			return false;
		}
		boolean inDelay = false;
		String key = buildCacheKey(delayKey);
		Object deadline = getDelayKeyFromCache(key);
		if (null != deadline) {
			inDelay = true;
		}
		logger.debug("delayKye:{}, isNeedDelayFree:{}", delayKey, inDelay);

		return inDelay;
	}

	@Override
	public void markNeedDelayFree(Object delayKey) {
		if (null == delayKey) {
			return;
		}
		String key = buildCacheKey(delayKey);
		storeDelayKeyToCache(key, getPeriod());
		logger.debug("mark delay free key:{}, period:{}", delayKey, getPeriod());
	}

	protected abstract String buildCacheKey(Object delayKey);

	/**
	 * 取标志
	 * 
	 * @param key
	 *            获取标志的键
	 * @return 返回值为null表示标志不存在，不需要处理delay
	 */
	protected abstract Object getDelayKeyFromCache(String key);

	/**
	 * 存标志
	 * 
	 * @param key
	 *            获取标志的键
	 * @param ttl
	 *            生存期，单位s
	 */
	protected abstract void storeDelayKeyToCache(String key, Long period);

}
