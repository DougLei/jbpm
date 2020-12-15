package com.douglei.bpm.process.handler.event.start;

import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.variable.VariableEntityMapHolder;
import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventExecuteParameter implements ExecuteParameter {
	private ProcessMetadata processMetadata;
	private StartParameter startParameter;
	
	public StartEventExecuteParameter(ProcessMetadata processMetadata, StartParameter startParameter) {
		this.processMetadata = processMetadata;
		this.startParameter = startParameter;
	}

	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	public StartParameter getStartParameter() {
		return startParameter;
	}
	public VariableEntityMapHolder getVariableEntityMapHolder() {
		return startParameter.getVariableEntityMapHolder();
	}
}
