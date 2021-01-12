package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;

/**
 * 单人认领策略
 * @author DougLei
 */
@Bean(clazz = ClaimPolicy.class)
public class ClaimBySinglePolicy implements ClaimPolicy{
	public static final String POLICY_NAME = "bySingle";
	
	@Override
	public String getName() {
		return POLICY_NAME;
	}

	@Override
	public boolean isValueRequired() {
		return false;
	}

	@Override
	public ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		if(claimedAssigneeList == null)
			return new ClaimResult(true, 0);
		return ClaimResult.CAN_NOT_CLAIM;
	}
}