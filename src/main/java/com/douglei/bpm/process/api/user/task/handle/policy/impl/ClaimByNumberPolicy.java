package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.parser.task.user.AssignNumberParser;

/**
 * 指定人数(百分比)的认领策略
 * @author DougLei
 */
@Bean(clazz = ClaimPolicy.class)
public class ClaimByNumberPolicy implements ClaimPolicy{
	
	@Autowired
	private AssignNumberParser assignNumberParser; // 语法格式同AssignNumber的格式一致, 所以这里直接进行复用
	
	@Override
	public String getName() {
		return "byNumber";
	}
	
	@Override
	public boolean validateValue(String value) {
		return assignNumberParser.parse(value) != null;
	}

	@Override
	public ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		int claimUpperLimit = calc(assignNumberParser.parse(value), unclaimAssigneeList, claimedAssigneeList, finishedAssigneeList);
		
		// 获得已经认领的数量
		int claimedCount = 0; 
		if(claimedAssigneeList != null)
			claimedCount += claimedAssigneeList.size();
		if(finishedAssigneeList != null)
			claimedCount += finishedAssigneeList.size();
		
		// 判断是否认领到上限
		if(claimedCount == claimUpperLimit)
			return ClaimResult.CAN_NOT_CLAIM;
		
		// 计算剩余可认领的数量
		int leftCount = claimUpperLimit-claimedCount;
		for (Assignee assignee : unclaimAssigneeList) {
			if(assignee.getUserId().equals(currentClaimUserId))
				leftCount--;
			if(leftCount == 0)
				break;
		}
		return new ClaimResult(true, leftCount);
	}
	
	// 计算可认领的数量上限
	private int calc(AssignNumber number, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		if(!number.isPercent())
			return number.getNumber();
		
		int total = unclaimAssigneeList.size();
		if(claimedAssigneeList != null)
			total += claimedAssigneeList.size();
		if(finishedAssigneeList != null)
			total += finishedAssigneeList.size();
		
		int upperLimit = total*number.getNumber();
		if(upperLimit%100 > 0 && number.isCeiling()) {
			upperLimit = upperLimit/100 + 1;
		}else {
			upperLimit = upperLimit/100;
		}
		if(upperLimit == 0)
			upperLimit = 1;
		return upperLimit;
	}
}
