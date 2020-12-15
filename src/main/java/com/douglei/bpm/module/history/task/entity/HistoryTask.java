package com.douglei.bpm.module.history.task.entity;

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
	public HistoryTask(int procdefId, int procinstId, TaskMetadata taskMetadata) {
		super(procdefId, procinstId, taskMetadata);
	}
	public HistoryTask(Task task) {
		this.procdefId = task.getProcdefId();
		this.procinstId = task.getProcinstId();
		this.key = task.getKey();
		this.name = task.getName();
		this.type = task.getType();
		this.startTime = task.getStartTime();
		this.endTime = new Date();
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
