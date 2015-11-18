package com.sogou.bizdev.compass.sample.jdbctemplate.sequence;

public interface SequenceGenerator {

	/**根据sequence获取序列值
	 * @param sequence
	 * @return
	 */
	public Long getSequence(String sequence);
}
