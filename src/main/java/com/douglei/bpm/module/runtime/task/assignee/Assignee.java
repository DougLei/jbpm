package com.douglei.bpm.module.runtime.task.assignee;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Assignee {
	protected int id;
	protected int taskId;
	protected String userId;
	protected int isHandling;
	protected Date startTime;
	
	public Assignee() {}
	public Assignee(String userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getIsHandling() {
		return isHandling;
	}
	public void setIsHandling(int isHandling) {
		this.isHandling = isHandling;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
