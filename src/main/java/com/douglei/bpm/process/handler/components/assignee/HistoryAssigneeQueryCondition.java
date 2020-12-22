package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class HistoryAssigneeQueryCondition {
	private String taskinstId;
	private List<String> parentUserIds;
	
	HistoryAssigneeQueryCondition(String taskinstId) {
		this.taskinstId = taskinstId;
	}

	public void addParentUserId(String parentUserId) {
		if(parentUserIds == null)
			parentUserIds = new ArrayList<String>();
		parentUserIds.add(parentUserId);
	}
	public void resetParentUserIds() {
		if(parentUserIds != null)
			parentUserIds.clear();
	}
	
	public String getTaskinstId() {
		return taskinstId;
	}
	public List<String> getParentUserIds() {
		return parentUserIds;
	}
}
