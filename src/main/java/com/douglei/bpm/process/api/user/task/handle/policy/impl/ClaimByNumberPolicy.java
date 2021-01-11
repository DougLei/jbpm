package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;

/**
 * 指定人数的认领策略
 * @author DougLei
 */
@Bean(clazz = ClaimPolicy.class)
public class ClaimByNumberPolicy implements ClaimPolicy{
	
	@Override
	public String getName() {
		return "byNumber";
	}
	
	@Override
	public boolean validateValue(String value) {
		// TODO Auto-generated method stub
		
		
		
		
		
		return false;
	}

	@Override
	public ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		// TODO Auto-generated method stub
		
		
		
		
		
		return null;
	}
}
