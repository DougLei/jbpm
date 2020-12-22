package com.douglei.bpm.module.runtime.task;

import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author DougLei
 */
public class Assignee {
	protected int id;
	protected String taskinstId;
	protected String groupId;
	protected String userId;
	protected String parentUserId;
	protected String remark;
	protected AssigneeMode mode;
	protected HandleState handleState;
	protected Date startTime;
	
	protected Attitude attitude;
	protected String suggest;
	protected Date finishedTime;
	
	public Assignee() {}
	public Assignee(String taskinstId, String userId) {
		this.taskinstId = taskinstId;
		this.groupId = UUID.randomUUID().toString();
		this.userId = userId;
		this.mode = AssigneeMode.ASSIGN;
		this.handleState = HandleState.UNCLAIM;
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
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Attitude getAttitudeInstance() {
		return attitude;
	}
	public void setAttitudeInstance(Attitude attitude) {
		this.attitude = attitude;
	}
	public String getAttitude() {
		if(attitude == null)
			return null;
		return attitude.name();
	}
	public void setAttitude(String attitude) {
		if(attitude == null)
			return;
		this.attitude = Attitude.valueOf(attitude);
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
}
