package com.douglei.bpm.process.handler.components.scheduler;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.components.assignee.Assigner;
import com.douglei.bpm.process.handler.components.assignee.Assigners;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter extends ExecuteParameter{
	private String procinstId;
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(String procinstId, Map<String, Object> variableMap, GeneralExecuteParameter executeParameter) {
		super.processMetadata = executeParameter.getProcessMetadata();
		super.assigners = executeParameter.getAssigners();
		this.procinstId = procinstId;
		this.variableMap = variableMap;
	}
	public TaskDispatchParameter(String procinstId, Map<String, Object> variableMap, ProcessMetadata processMetadata, List<Assigner> assigners) {
		super.processMetadata = processMetadata;
		super.assigners = new Assigners(assigners);
		this.procinstId = procinstId;
		this.variableMap = variableMap;
	}

	public Map<String, Object> getVariableMap() {
		return variableMap;
	}

	@Override
	public String getProcinstId() {
		return procinstId;
	}
}
