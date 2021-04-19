package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class TaskEntity {
	private String taskinstId;
	private String parentTaskinstId;
	private String sourceKey;
	private String key;
	
	public TaskEntity() {}
	public TaskEntity(Task task, String key) {
		this.taskinstId = task.getTaskinstId();
		this.parentTaskinstId = task.getParentTaskinstId();
		this.sourceKey = task.getSourceKey();
		this.key = key;
	}
	
	/**
	 * 是否是StartEvent
	 * @param process
	 * @return
	 */
	public boolean isStartEvent(ProcessMetadata process) {
		return process.getStartEventMetadata().getId().equals(key);
	}
	
	public String getTaskinstId() {
		return taskinstId;
	}
	public void setTaskinstId(String taskinstId) {
		this.taskinstId = taskinstId;
	}
	public String getParentTaskinstId() {
		return parentTaskinstId;
	}
	public void setParentTaskinstId(String parentTaskinstId) {
		this.parentTaskinstId = parentTaskinstId;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getKey() {
		return key;
	}
	public void setKey_(String key) {
		this.key = key;
	}
}
