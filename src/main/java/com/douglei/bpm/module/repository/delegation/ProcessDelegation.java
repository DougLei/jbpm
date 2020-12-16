package com.douglei.bpm.module.repository.delegation;

import java.util.Date;

/**
 * 流程委托
 * @author DougLei
 */
public class ProcessDelegation {
	private int id;
	private String clientId;
	private String trusteeId;
	private Date startDate;
	private Date endDate;
	private String reason;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getTrusteeId() {
		return trusteeId;
	}
	public void setTrusteeId(String trusteeId) {
		this.trusteeId = trusteeId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
