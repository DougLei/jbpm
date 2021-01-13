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
	protected Integer forkChainId;
	protected String userId;
	protected String remark;
	protected AssignMode mode;
	protected HandleState handleState;
	protected Date claimTime;
	private long claimTime_;
	
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
	public Integer getForkChainId() {
		return forkChainId;
	}
	public void setForkChainId(Integer forkChainId) {
		this.forkChainId = forkChainId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public Assignee setHandleStateInstance(HandleState handleState) {
		this.handleState = handleState;
		return this;
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
	public Assignee setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
		return this;
	}
	public long getClaimTime_() {
		if(claimTime != null && claimTime_ == 0)
			claimTime_ = claimTime.getTime();
		return claimTime_;
	}
}
