package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
class TaskEntity {
	private String taskinstId;
	private String sourceKey;
	private String key;
	
	public TaskEntity() {}
	public TaskEntity(Task task, String key) {
		this.taskinstId = task.getTaskinstId();
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
