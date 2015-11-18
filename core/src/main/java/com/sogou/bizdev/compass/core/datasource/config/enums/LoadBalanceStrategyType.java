package com.sogou.bizdev.compass.core.datasource.config.enums;

import org.springframework.util.StringUtils;

/**
 * 内置负载均衡器
 * 
 * @author gly
 * @since 1.0.0
 */
public enum LoadBalanceStrategyType 
{
	FASTEST("fastest"),
	LEAST_ACTIVE("leastActive"),
	WEIGHTED_RANDOM("weightedRandom"),
	WEIGHTED_ROUND_ROBIN("weightedRoundRobin");
	
	String value;

	public String getValue() 
	{
		return value;
	}

	private LoadBalanceStrategyType(String value) 
	{
		this.value = value;
	}
	
	public static LoadBalanceStrategyType parse(String input) 
	{
		if(!StringUtils.hasText(input))
		{
			return null;
		}
		input=input.trim();
		for (LoadBalanceStrategyType type : LoadBalanceStrategyType.values()) 
		{
			if (type.getValue().equalsIgnoreCase(input))
			{
				return type;
			}
		}
		return null;
	} 
	 
}
