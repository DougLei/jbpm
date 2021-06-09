package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.HashSet;

/**
 * 指派人委托查询条件
 * @author DougLei
 */
public class SqlCondition {
	private long currentTime;
	private String procdefCode;
	private String procdefVersion;
	private HashSet<String> userIds;
	
	public SqlCondition(String procdefCode, String procdefVersion, HashSet<String> assignedUserIds) {
		this.currentTime = System.currentTimeMillis();
		this.procdefCode = procdefCode;
		this.procdefVersion = procdefVersion;
		this.userIds = assignedUserIds;
	}

	/**
	 * 更新userId集合
	 * @param userIds
	 */
	public void updateUserIds(HashSet<String> userIds) {
		this.userIds = userIds;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}
	public String getProcdefCode() {
		return procdefCode;
	}
	public String getProcdefVersion() {
		return procdefVersion;
	}
	public HashSet<String> getUserIds() {
		return userIds;
	}
}
