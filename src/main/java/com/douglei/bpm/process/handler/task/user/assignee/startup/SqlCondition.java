package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.HashSet;

/**
 * 指派人委托查询条件
 * @author DougLei
 */
public class SqlCondition {
	private long currentTime;
	private HashSet<String> userIds;
	
	public SqlCondition(HashSet<String> assignedUserIds) {
		this.currentTime = System.currentTimeMillis();
		this.userIds = assignedUserIds;
	}

	public long getCurrentTime() {
		return currentTime;
	}
	public HashSet<String> getUserIds() {
		return userIds;
	}
	public void updateUserIds(HashSet<String> userIds) {
		this.userIds = userIds;
	}
}
