package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class HistoryTask extends Task{
	private Date endTime;

	public HistoryTask() {}
	public HistoryTask(String procinstId, String parentTaskinstId, Date endTime, TaskMetadata taskMetadata, ProcessMetadata metadata) {
		super(procinstId, parentTaskinstId, endTime, taskMetadata, metadata);
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
