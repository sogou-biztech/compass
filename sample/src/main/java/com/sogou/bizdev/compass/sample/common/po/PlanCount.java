package com.sogou.bizdev.compass.sample.common.po;

/**
 * 记录一个accountId下的cpcplanid的数目
 * @author yanke
 * @since 1.0.0
 */
public class PlanCount {
	
	private Long accountId;
	
	private Integer planCount;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getPlanCount() {
		return planCount;
	}

	public void setPlanCount(Integer planCount) {
		this.planCount = planCount;
	}

}
