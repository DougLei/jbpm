package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class ClaimTaskParameter {
	private Date claimTime = new Date();
	private List<Integer> assigneeIds;
	private String taskinstId;
	private List<Integer> groupIds;
	
	ClaimTaskParameter(int assigneeCount, String taskinstId){
		this.assigneeIds = new ArrayList<Integer>(assigneeCount);
		this.taskinstId = taskinstId;
	}

	public void addAssigneeId(int assigneeId) {
		assigneeIds.add(assigneeId);
	}
	public void addGroupId(int groupId) {
		if(groupIds == null)
			groupIds = new ArrayList<Integer>();
		groupIds.add(groupId);
	}
	
	public Date getClaimTime() {
		return claimTime;
	}
	public List<Integer> getAssigneeIds() {
		return assigneeIds;
	}
	public String getTaskinstId() {
		return taskinstId;
	}
	public List<Integer> getGroupIds() {
		return groupIds;
	}
}
