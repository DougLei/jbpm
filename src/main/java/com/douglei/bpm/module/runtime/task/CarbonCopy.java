package com.douglei.bpm.module.runtime.task;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class CarbonCopy {
	protected int id;
	protected String taskinstId;
	protected String ccUserId;
	protected Date ccTime;
	protected String userId;
	
	public CarbonCopy() {}
	public CarbonCopy(String taskinstId, String ccUserId, Date ccTime, String userId) {
		this.taskinstId = taskinstId;
		this.ccUserId = ccUserId;
		this.ccTime = ccTime;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTaskinstId() {
		return taskinstId;
	}
	public void setTaskinstId(String taskinstId) {
		this.taskinstId = taskinstId;
	}
	public String getCcUserId() {
		return ccUserId;
	}
	public void setCcUserId(String ccUserId) {
		this.ccUserId = ccUserId;
	}
	public Date getCcTime() {
		return ccTime;
	}
	public void setCcTime(Date ccTime) {
		this.ccTime = ccTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
