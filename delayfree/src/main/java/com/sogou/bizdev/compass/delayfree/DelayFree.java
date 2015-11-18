package com.sogou.bizdev.compass.delayfree;

/**
 * <p>
 * 解决主从延迟方案,方案的总体思路是如果进行了delayKey的写操作，标记period时间范围的后续delayKey的操作都强制去主库
 * </p>
 * 
 * @author xr
 * @since 1.0.0
 */
public interface DelayFree {
	/**
	 * 判断是否需要做延迟处理
	 * 
	 * @param delayKey
	 *            延迟处理的粒度，比如账户范围，antiDelayKey就是账户id
	 * @return
	 */
	public boolean isInDelay(Object antiDelayKey);

	/**
	 * 标记需要做延迟处理
	 * 
	 * @param delayKey
	 *            延迟处理的粒度，比如账户范围，antiDelayKey就是账户id
	 */
	public void markNeedDelayFree(Object antiDelayKey);
}
