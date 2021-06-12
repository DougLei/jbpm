package com.douglei.bpm.module.repository.delegation;

import java.util.HashSet;

/**
 * 
 * @author DougLei
 */
public class DelegationSqlCondition {
	private long startTime;
	private long endTime;
	private HashSet<String> userIds;
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public DelegationSqlCondition(long startTime, long endTime, String userId) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.userIds = new HashSet<String>(2);
		this.userIds.add(userId);
	}

	/**
	 * 重置userId集合
	 * @param userIds
	 */
	public HashSet<String> resetUserIds() {
		userIds.clear();
		return userIds;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public HashSet<String> getUserIds() {
		return userIds;
	}
}
