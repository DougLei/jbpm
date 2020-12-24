package com.douglei.bpm.module.repository.delegation;

import java.util.Date;

/**
 * 流程委托
 * @author DougLei
 */
public class ProcessDelegation {
	private int id;
	private String userId;
	private String assignedUserId;
	private Long startTime;
	private Long endTime;
	private String reason;
	private Date acceptTime;
	private int isEnabled;
	private String tenantId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}
	public boolean isEnabled() {
		return isEnabled == 1;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
