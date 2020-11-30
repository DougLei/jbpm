package com.douglei.bpm.module.runtime.task.entity;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Assignee {
	protected int id;
	protected int taskId;
	protected int mode;
	protected String userId;
	protected int isHandling;
	protected Date startTime;
	
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
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
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
