package com.douglei.bpm.process.handler.event.start;

import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.variable.VariableEntityMapHolder;
import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventExecuteParameter extends ExecuteParameter {
	private StartParameter startParameter;
	
	public StartEventExecuteParameter(ProcessMetadata processMetadata, StartParameter startParameter) {
		super.processMetadata = processMetadata;
		this.startParameter = startParameter;
	}

	public StartParameter getStartParameter() {
		return startParameter;
	}
	public VariableEntityMapHolder getVariableEntityMapHolder() {
		return startParameter.getVariableEntityMapHolder();
	}

	@Override
	public String getProcinstId() {
		throw new IllegalArgumentException("StartEvent的执行参数, 不存在流程实例id");
	}
}
