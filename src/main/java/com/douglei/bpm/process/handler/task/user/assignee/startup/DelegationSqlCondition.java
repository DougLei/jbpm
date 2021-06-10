package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.HashSet;

/**
 * 
 * @author DougLei
 */
public class DelegationSqlCondition {
	private long currentTime;
	private String procdefCode;
	private String procdefVersion;
	private HashSet<String> userIds;
	
	public DelegationSqlCondition(String procdefCode, String procdefVersion, HashSet<String> userIds) {
		this.currentTime = System.currentTimeMillis();
		this.procdefCode = procdefCode;
		this.procdefVersion = procdefVersion;
		this.userIds = userIds;
	}

	/**
	 * 重置userId集合
	 * @param userIds
	 */
	public void resetUserIds(HashSet<String> userIds) {
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
