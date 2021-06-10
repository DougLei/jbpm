package com.douglei.bpm.process.handler.task.user.assignee.startup;

/**
 * 
 * @author DougLei
 */
public class DelegationInfo {
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 被委托的用户id
	private String reason; // 委托原因
	
	@Override
	public boolean equals(Object obj) {
		return userId.equals(((DelegationInfo)obj).userId);
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAssignedUserId() {
		return assignedUserId;
	}
	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
