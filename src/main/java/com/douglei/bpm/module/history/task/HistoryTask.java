package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class HistoryTask extends Task{
	private Date endTime;

	public HistoryTask() {}
	public HistoryTask(int procdefId, String procinstId, TaskMetadata taskMetadata) {
		super(procdefId, procinstId, taskMetadata);
		this.endTime = super.startTime;
	}
	public HistoryTask(Task task) {
		super.procdefId = task.getProcdefId();
		super.procinstId = task.getProcinstId();
		super.taskinstId = task.getTaskinstId();
		super.key = task.getKey();
		super.name = task.getName();
		super.type = task.getType();
		super.startTime = task.getStartTime();
		super.expiryTime = task.getExpiryTime();
		super.businessId = task.getBusinessId();
		super.pageId = task.getPageId();
		super.groupId = task.getGroupId();
		this.endTime = new Date();
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
