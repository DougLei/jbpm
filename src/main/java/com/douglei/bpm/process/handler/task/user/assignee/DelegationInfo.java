package com.douglei.bpm.process.handler.task.user.assignee;

/**
 * 委托信息
 * @author DougLei
 */
public class DelegationInfo {
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 被委托的用户id
	private String reason;
	private String procdefCode;
	private String procdefVersion;
	
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
	public String getProcdefCode() {
		return procdefCode;
	}
	public void setProcdefCode(String procdefCode) {
		this.procdefCode = procdefCode;
	}
	public String getProcdefVersion() {
		return procdefVersion;
	}
	public void setProcdefVersion(String procdefVersion) {
		this.procdefVersion = procdefVersion;
	}
}
