package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class HistoryTask extends Task{
	private Date endTime;

	public HistoryTask() {}
	public HistoryTask(int procdefId, String procinstId, String parentTaskinstId, Date endTime, String sourceKey, TaskMetadata taskMetadata) {
		super(procdefId, procinstId, parentTaskinstId, endTime, sourceKey, taskMetadata);
		this.endTime = endTime;
	}
	public HistoryTask(Task task, Date endTime) {
		PropertyValueCopier.copy(task, this);
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
