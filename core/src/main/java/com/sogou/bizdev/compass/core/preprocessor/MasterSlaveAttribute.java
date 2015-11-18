/**
 * 
 */
package com.sogou.bizdev.compass.core.preprocessor;

/**
 * @Description: 主从属性
 * @author zjc
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
