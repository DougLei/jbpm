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
	protected int chainId;
	private Integer isChainLast;
	protected String userId;
	protected String reason;
	protected AssignMode mode;
	protected HandleState handleState;
	protected Date claimTime;
	
	public Assignee() {}
	public Assignee(String taskinstId, String userId, int groupId, int chainId) {
		this.taskinstId = taskinstId;
		this.userId = userId;
		this.groupId = groupId;
		this.chainId = chainId;
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
	/**
	 * 是否是chain的第一个
	 * @return
	 */
	public boolean isChainFirst() {
		return chainId == 0;
	}
	public int getChainId() {
		return chainId;
	}
	public void setChainId(int chainId) {
		this.chainId = chainId;
	}
	/**
	 * 是否是chain的最后一个
	 * @return
	 */
	public boolean isChainLast() {
		return isChainLast !=null && isChainLast == 1;
	}
	public Integer getIsChainLast() {
		return isChainLast;
	}
	public void setIsChainLast(Integer isChainLast) {
		this.isChainLast = isChainLast;
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
	public AssignMode getModeInstance() {
		return mode;
	}
	public void setModeInstance(AssignMode mode) {
		this.mode = mode;
	}
	public Integer getMode() {
		if(mode == null)
			return null;
		return mode.getValue();
	}
	public void setMode_(int mode) {
		this.mode = AssignMode.valueOf(mode) ;
	}
	public HandleState getHandleStateInstance() {
		return handleState;
	}
	public void setHandleStateInstance(HandleState handleState) {
		this.handleState = handleState;
	}
	public Integer getHandleState() {
		if(handleState == null)
			return null;
		return handleState.getValue();
	}
	public void setHandleState(int handleState) {
		this.handleState = HandleState.valueOf(handleState);
	}
	public Date getClaimTime() {
		return claimTime;
	}
	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}
	
	private long claimTime_;
	public long getClaimTime_() {
		if(claimTime != null && claimTime_ == 0)
			claimTime_ = claimTime.getTime();
		return claimTime_;
	}
	
	/**
	 * 进行认领
	 * @param claimTime
	 */
	public void claim(Date claimTime) {
		this.handleState = HandleState.CLAIMED;
		this.claimTime = claimTime;
		this.isChainLast=1;
	}
}
