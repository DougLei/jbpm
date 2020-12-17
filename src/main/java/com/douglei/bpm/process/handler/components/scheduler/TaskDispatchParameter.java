package com.douglei.bpm.process.handler.components.scheduler;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.components.assignee.Assigner;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter extends ExecuteParameter{
	private int procinstId;
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(int procinstId, Map<String, Object> variableMap, GeneralExecuteParameter executeParameter) {
		super(executeParameter.getProcessMetadata(), executeParameter.getAssigners());
		this.procinstId = procinstId;
		this.variableMap = variableMap;
	}
	public TaskDispatchParameter(int procinstId, Map<String, Object> variableMap, ProcessMetadata processMetadata, List<Assigner> assigners) {
		super(processMetadata, assigners);
		this.procinstId = procinstId;
		this.variableMap = variableMap;
	}

	public Map<String, Object> getVariableMap() {
		return variableMap;
	}

	@Override
	public int getProcinstId() {
		return procinstId;
	}
}
