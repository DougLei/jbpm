package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.List;

/**
 * 指派人委托查询条件
 * @author DougLei
 */
public class SqlCondition {
	private long currentTime;
	private List<String> userIds;
	
	public SqlCondition(List<String> assignedUserIds) {
		this.currentTime = System.currentTimeMillis();
		this.userIds = assignedUserIds;
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
