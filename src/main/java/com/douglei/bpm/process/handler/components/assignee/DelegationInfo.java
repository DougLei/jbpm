package com.douglei.bpm.process.handler.components.assignee;

/**
 * 委托信息
 * @author DougLei
 */
public class DelegationInfo {
	private String clientId;
	private String assigneeId;
	private String reason;
	private String procdefCode;
	private String procdefVersion;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
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
