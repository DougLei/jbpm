package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;
import com.douglei.bpm.process.mapping.parser.task.user.AssignNumberParser;

/**
 * 指定人数(/百分比)的认领(的策略)
 * @author DougLei
 */
@Bean(clazz = ClaimPolicy.class)
public class ClaimByNumberPolicy implements ClaimPolicy{
	public static final String NAME = "byNumber";
	
	@Autowired
	private AssignNumberParser assignNumberParser; // 语法格式同AssignNumber的格式一致, 所以这里直接进行复用
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean validateValue(String value) {
		return assignNumberParser.parse(value) != null;
	}
	
	@Override
	public boolean tryAutoClaim(String value, int assignCount, List<Assignee> assigneeList, Date claimTime) {
		if(assignCount > calcUpperLimit(value, assignCount)) 
			return false;
		
		assigneeList.stream().filter(assignee -> assignee.isChainLast()).forEach(assignee -> assignee.claim(claimTime));
		return true;
	}
	
	@Override
	public int calcUpperLimit(String value, int assignCount) {
		return assignNumberParser.parse(value).calcUpperLimit(assignCount);
	}
	
	@Override
	public ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> currentAssigneeList, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList) {
		// 获得已经认领的数量
		int claimedCount = 0; 
		if(claimedAssigneeList != null)
			claimedCount += claimedAssigneeList.size();
		if(finishedAssigneeList != null)
			claimedCount += finishedAssigneeList.size();
		
		// 判断是否认领到上限
		int claimUpperLimit = calcUpperLimit(value, unclaimAssigneeList.size()+claimedCount);
		if(claimedCount == claimUpperLimit)
			return ClaimResult.CAN_NOT_CLAIM;
		
		// 计算剩余可认领的数量
		int leftCount = claimUpperLimit-claimedCount;
		for (Assignee assignee : unclaimAssigneeList) {
			if(assignee.getUserId().equals(currentClaimUserId) || inSameGroup(assignee.getGroupId(), currentAssigneeList))
				leftCount--;
			if(leftCount == 0)
				break;
		}
		return new ClaimResult(true, leftCount);
	}
	
	// 判断未认领的指派信息的gourpId, 是否存在于当前进行认领的用户的指派信息中
	private boolean inSameGroup(int groupId, List<Assignee> currentAssigneeList) {
		for (Assignee assignee : currentAssigneeList) {
			if(groupId == assignee.getGroupId())
				return true;
		}
		return false;
	}
}
