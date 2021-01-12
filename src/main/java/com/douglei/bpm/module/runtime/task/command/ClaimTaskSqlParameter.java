package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Assignee;

/**
 * 
 * @author DougLei
 */
public class ClaimTaskSqlParameter {
	private Date claimTime = new Date();
	private List<Integer> assigneeIds;
	private String taskinstId;
	private List<Integer> groupIds;
	
	ClaimTaskSqlParameter(int assigneeCount, String taskinstId){
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
	
	
	// -------------------------------------------------------------------------------------
	// 查询同组内有没有人已经认领时用到的参数
	// -------------------------------------------------------------------------------------
	private List<Assignee> assigneeList;
	public List<Assignee> getAssigneeList() {
		return assigneeList;
	}
	public void setAssigneeList(List<Assignee> assigneeList) {
		this.assigneeList = assigneeList;
	}
}
