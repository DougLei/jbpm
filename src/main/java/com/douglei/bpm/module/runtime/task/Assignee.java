package com.douglei.bpm.module.runtime.task;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class Assignee {
	protected int id;
	protected String taskinstId;
	protected int groupId;
	protected String userId;
	protected String parentUserId;
	protected String remark;
	protected AssignMode mode;
	protected HandleState handleState;
	protected Date claimTime;
	private long claimTime_;
	
	public Assignee() {}
	public Assignee(String taskinstId, String userId, int groupId) {
		this.taskinstId = taskinstId;
		this.groupId = groupId;
		this.userId = userId;
		this.mode = AssignMode.ASSIGNED;
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
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
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
	public AssignMode getModeInstance() {
		return mode;
	}
	public void setModeInstance(AssignMode mode) {
		this.mode = mode;
	}
	public String getMode() {
		return mode.name();
	}
	public void setMode(String mode) {
		this.mode = AssignMode.valueOf(mode) ;
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
	public Date getClaimTime() {
		return claimTime;
	}
	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}
	public long getClaimTime_() {
		if(claimTime != null && claimTime_ == 0)
			claimTime_ = claimTime.getTime();
		return claimTime_;
	}
}
