package com.sogou.bizdev.compass.sample.hibernate.combined;

import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.sample.common.po.Plan;

public interface ShardFirstService {
	public Plan getMSAndShardPlan(@RouteKey Long accountId, Long planId);
}
