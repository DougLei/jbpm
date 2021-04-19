package com.douglei.bpm.process.handler.task.user.assignee.handle;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.execution.task.runtime.Assignee;

/**
 * 根据groupId查询指派信息的sql条件类
 * @author DougLei
 */
public class AssigneeQueryByGroupIdSqlCondition {
	private String taskinstId;
	private List<Integer> groupIds;
	
	public AssigneeQueryByGroupIdSqlCondition(String taskinstId, List<? extends Assignee> list) {
		this.taskinstId = taskinstId;
		this.groupIds = new ArrayList<Integer>(list.size());
		list.forEach(l -> {
			if(!groupIds.contains(l.getGroupId()))
				groupIds.add(l.getGroupId());
		});
	}

	public String getTaskinstId() {
		return taskinstId;
	}
	public List<Integer> getGroupIds() {
		return groupIds;
	}
}
