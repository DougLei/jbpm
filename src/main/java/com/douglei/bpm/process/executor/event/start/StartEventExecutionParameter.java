package com.douglei.bpm.process.executor.event.start;

import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.executor.ProcessExecutionParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventExecutionParameter implements ProcessExecutionParameter {
	private ProcessMetadata processMetadata;
	private StartParameter startParameter;
	
	public StartEventExecutionParameter(ProcessMetadata processMetadata, StartParameter startParameter) {
		this.processMetadata = processMetadata;
		this.startParameter = startParameter;
	}

	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	public StartParameter getStartParameter() {
		return startParameter;
	}
}
