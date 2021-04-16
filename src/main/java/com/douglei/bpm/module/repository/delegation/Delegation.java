package com.douglei.bpm.module.repository.delegation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程委托
 * @author DougLei
 */
public class Delegation {
	private int id;
	private String userId;
	private String assignedUserId;
	private long startTime;
	private long endTime;
	private String reason;
	private Date acceptTime;
	private int isEnabled;
	private String tenantId;
	private List<DelegationDetail> details;
	
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public boolean isAccept() {
		return acceptTime != null;
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
	public List<DelegationDetail> getDetails() {
		return details;
	}
	public void setDetails(List<DelegationDetail> details) {
		this.details = details;
	}
	public void addDetail(DelegationDetail detail) {
		if(details == null)
			details = new ArrayList<DelegationDetail>();
		details.add(detail);
	}
}
