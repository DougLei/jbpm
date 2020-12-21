package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.components.assignee.Assigners;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter extends ExecuteParameter{
	private Task taskInstance;
	
	public GeneralExecuteParameter(Task taskInstance, ProcessMetadata processMetadata, Assigners assigners) {
		super.processMetadata = processMetadata;
		super.assigners = assigners;
		this.taskInstance = taskInstance;
	}
	
	public Task getTaskInstance() {
		return taskInstance;
	}
	@Override
	public String getProcinstId() {
		return taskInstance.getProcinstId();
	}
}
