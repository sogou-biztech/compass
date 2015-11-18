/**
 * 
 */
package com.sogou.bizdev.compass.core.preprocessor;

/**
 * @Description: 主从属性
 * @author zhangjuncheng@sogou-inc.com
 * @date 2014-5-7 下午5:29:12
 * @since 1.0.0
 */
public enum MasterSlaveAttribute {

	MASTER("MASTER"), SLAVE("SLAVE");

	String value;

	private MasterSlaveAttribute(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
