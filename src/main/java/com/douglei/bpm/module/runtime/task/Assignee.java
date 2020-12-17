package com.douglei.bpm.module.runtime.task;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Assignee {
	protected int id;
	protected int taskId;
	protected int order;
	protected String userId;
	protected String parentUserId;
	protected AssigneeMode mode;
	protected HandleState handleState;
	protected Date startTime;
	protected Date finishedTime;
	
	public Assignee() {}
	public Assignee(int taskId, String userId) {
		this.taskId = taskId;
		this.userId = userId;
		this.mode = AssigneeMode.ASSIGN;
		this.handleState = HandleState.UNHANDLE;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getParentUserId() {
		return parentUserId;
	}
	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}
	public AssigneeMode getModeInstance() {
		return mode;
	}
	public void setModeInstance(AssigneeMode mode) {
		this.mode = mode;
	}
	public String getMode() {
		return mode.name();
	}
	public void setMode(String mode) {
		this.mode = AssigneeMode.valueOf(mode) ;
	}
	public HandleState getHandleStateInstance() {
		return handleState;
	}
	public void setHandleStateInstance(HandleState handleState) {
		this.handleState = handleState;
	}
	public String getHandleState() {
		return handleState.name();
	}
	public void setHandleState(String handleState) {
		this.handleState = HandleState.valueOf(handleState);
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
}
