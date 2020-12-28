package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.handler.User;

/**
 * 指派人委托查询条件
 * @author DougLei
 */
public class DelegationSqlQueryCondition {
	private long currentTime;
	private List<String> userIds;
	
	DelegationSqlQueryCondition(long currentTime, List<User> assignedUsers) {
		this.currentTime = currentTime;
		this.userIds = new ArrayList<String>(assignedUsers.size());
		assignedUsers.forEach(assignedUser -> this.userIds.add(assignedUser.getUserId()));
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
