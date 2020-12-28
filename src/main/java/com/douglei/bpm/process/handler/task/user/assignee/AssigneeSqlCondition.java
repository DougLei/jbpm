package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.history.task.HistoryAssignee;

/**
 * (存储和传递) 运行表中的指派信息sql条件
 * @author DougLei
 */
public class AssigneeSqlCondition {
	private String taskinstId;
	private List<Integer> groupIds;
	private List<HistoryAssignee> assigneeList;
	
	AssigneeSqlCondition(String taskinstId, List<HistoryAssignee> assigneeList) {
		this.taskinstId = taskinstId;
		this.groupIds = new ArrayList<Integer>(assigneeList.size());
		for (HistoryAssignee historyAssignee : assigneeList) 
			groupIds.add(historyAssignee.getGroupId());
		this.assigneeList = assigneeList;
	}
	void updateAssigneeList(List<HistoryAssignee> assigneeList) {
		this.assigneeList = assigneeList;
	}
	
	public String getTaskinstId() {
		return taskinstId;
	}
	public List<Integer> getGroupIds() {
		return groupIds;
	}
	public List<HistoryAssignee> getAssigneeList() {
		return assigneeList;
	}
}
