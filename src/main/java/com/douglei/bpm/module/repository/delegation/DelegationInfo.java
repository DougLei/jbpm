package com.douglei.bpm.module.repository.delegation;

/**
 * 委托信息
 * @author DougLei
 */
public class DelegationInfo {
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 被委托的用户id
	private long startTime; // 开始时间
	private long endTime; // 结束时间
	private String procdefCode; // 流程定义的code
	private String procdefVersion; // 流程定义的version
	
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
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
