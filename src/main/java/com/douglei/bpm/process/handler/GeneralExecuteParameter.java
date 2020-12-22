package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.bpm.process.handler.components.assignee.Assigners;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter extends ExecuteParameter{
	private Task taskInstance;
	
	public GeneralExecuteParameter(TaskEntity taskInstance, Assigners assigners) {
		super.processMetadata = taskInstance.getProcessMetadata();
		super.assigners = assigners;
		this.taskInstance = taskInstance.getTask();
	}
	
	public Task getTaskInstance() {
		return taskInstance;
	}
	@Override
	public String getProcinstId() {
		return taskInstance.getProcinstId();
	}
}
