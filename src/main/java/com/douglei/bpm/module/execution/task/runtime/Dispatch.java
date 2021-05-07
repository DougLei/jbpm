package com.douglei.bpm.module.execution.task.runtime;

/**
 * 
 * @author DougLei
 */
public class Dispatch {
	protected int id;
	protected String taskinstId;
	protected int chainId;
	protected String userId;
	protected String reason;
	protected int isEnabled;
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean isEnabled() {
		return isEnabled == 1;
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
	public int getChainId() {
		return chainId;
	}
	public void setChainId(int chainId) {
		this.chainId = chainId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
}
