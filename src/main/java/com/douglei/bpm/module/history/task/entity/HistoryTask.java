package com.douglei.bpm.module.history.task.entity;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class HistoryTask extends Task{
	private Date endTime;

	public HistoryTask() {}
	public HistoryTask(int procdefId, int procinstId, TaskNodeMetadata taskMetadata) {
		super(procdefId, procinstId, taskMetadata);
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
