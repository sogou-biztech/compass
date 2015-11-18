package com.sogou.bizdev.compass.sample.hibernate.combined;

import com.sogou.bizdev.compass.sample.common.po.Plan;

public interface MSFirstService {
	public Plan getMSAndShardPlan(Long accountId, Long planId);
}
