package com.douglei.bpm.process.executor.event.start;

import com.douglei.bpm.module.runtime.instance.command.start.process.StartParameter;
import com.douglei.bpm.process.executor.ExecutionParameter;

/**
 * 
 * @author DougLei
 */
public class StartEventExecutionParameter implements ExecutionParameter {
	private int processDefinitionId;
	private int processInstanceId;
	private StartParameter startParameter;
	
	public StartEventExecutionParameter(int processDefinitionId, int processInstanceId, StartParameter startParameter) {
		this.processDefinitionId = processDefinitionId;
		this.processInstanceId = processInstanceId;
		this.startParameter = startParameter;
	}

	public int getProcessDefinitionId() {
		return processDefinitionId;
	}
	public int getProcessInstanceId() {
		return processInstanceId;
	}
	public StartParameter getStartParameter() {
		return startParameter;
	}
}
