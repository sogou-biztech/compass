package com.sogou.bizdev.compass.core.selector.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.util.CollectionUtils;

/**
 * 带权随机，是一种不带状态的均衡策略
 * @author xr
 * @since 1.0.0
 */
public class WeightedRandom implements LoadBalance 
{

	public static final String NAME = "random";
	private final Random random = new Random();
	
	@Override
	public <T extends Selectable> T select(List<T> targets)
	{
		if( CollectionUtils.isEmpty(targets))
		{
			throw new IllegalArgumentException("targets is empty");
		}
		List<T> filteredTarget = this.getWeightedRoundRobinList(targets);
	    int position = random.nextInt(filteredTarget.size());
	    return filteredTarget.get(position);
	}

	private <T extends Selectable> List<T> getWeightedRoundRobinList(List<T> targets)
	{
		List<T> result = new ArrayList<T>();
		int time = getMaxWeight(targets);
		for( int i=0; i<time; i++)
		{
			for( T target : targets )
			{
				if( target.getWeight() > i )
				{
					result.add(target);
				}
			}
		}
		return result;
	}
	
	private <T extends Selectable> int getMaxWeight(List<T> targets)
	{
		int max = 0;
		for( T target : targets)
		{
			if( max < target.getWeight())
			{
				max = target.getWeight();
			}
		}
		return max;
	}
}
