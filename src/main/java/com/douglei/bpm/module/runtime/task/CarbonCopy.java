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
	protected String acceptUserId;
	
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
	public String getAcceptUserId() {
		return acceptUserId;
	}
	public void setAcceptUserId(String acceptUserId) {
		this.acceptUserId = acceptUserId;
	}
}
