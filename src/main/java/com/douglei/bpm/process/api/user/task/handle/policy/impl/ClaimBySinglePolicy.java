package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.Date;
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
	
	@Override
	public String getName() {
		return "bySingle";
	}

	@Override
	public boolean valueIsRequired() {
		return false;
	}
	
	@Override
	public boolean tryAutoClaim(String value, int assignCount, List<Assignee> assigneeList, Date claimTime) {
		if(assignCount > 1)
			return false;
		
		assigneeList.get(assigneeList.size()-1).claim(claimTime);
		return true;
	}

	@Override
	public ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> currentAssigneeList, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		if(claimedAssigneeList == null)
			return new ClaimResult(true, 0);
		return ClaimResult.CAN_NOT_CLAIM;
	}
}