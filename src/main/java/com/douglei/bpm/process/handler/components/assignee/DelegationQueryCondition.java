package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.List;

/**
 * 指派人委托查询器
 * @author DougLei
 */
public class DelegationQueryCondition {
	private long currentTime;
	private List<String> userIds;
	
	DelegationQueryCondition(long currentTime, Assigners assigners) {
		this.userIds = new ArrayList<String>(assigners.size());
		assigners.getList().forEach(assigner -> this.userIds.add(assigner.getUserId()));
	}

	public long getCurrentTime() {
		return currentTime;
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void updateUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
}
