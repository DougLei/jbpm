package com.douglei.bpm.process.handler;

import java.util.List;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.components.assignee.Assigner;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter extends ExecuteParameter{
	private Task taskInstance;
	
	public GeneralExecuteParameter(Task taskInstance) {
		this(taskInstance, null, null);
	}
	public GeneralExecuteParameter(Task taskInstance, ProcessMetadata processMetadata, List<Assigner> assigners) {
		super(processMetadata, assigners);
		this.taskInstance = taskInstance;
	}
	
	public Task getTaskInstance() {
		return taskInstance;
	}
	@Override
	public int getProcinstId() {
		return taskInstance.getProcinstId();
	}
}
