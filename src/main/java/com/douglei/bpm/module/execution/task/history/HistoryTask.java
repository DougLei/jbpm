package com.douglei.bpm.module.execution.task.history;

import java.util.Date;

import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class HistoryTask extends Task{
	private Date endTime;
	private SourceType sourceType;

	public HistoryTask() {}
	public HistoryTask(int procdefId, String procinstId, String parentTaskinstId, String sourceKey, TaskMetadata taskMetadata) {
		super(procdefId, procinstId, parentTaskinstId, sourceKey, taskMetadata);
		this.endTime = this.startTime;
		this.sourceType = SourceType.STANDARD;
	}
	public HistoryTask(Task task) {
		PropertyValueCopier.copy(task, this);
		this.endTime = new Date();
		this.sourceType = SourceType.STANDARD;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getSourceType() {
		if(sourceType == null)
			return null;
		return sourceType.getValue();
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = SourceType.valueOf(sourceType);
	}
	public SourceType getSourceTypeInstance() {
		return sourceType;
	}
	public void setSourceTypeInstance(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
