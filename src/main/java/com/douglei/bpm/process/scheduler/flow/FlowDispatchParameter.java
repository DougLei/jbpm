package com.douglei.bpm.process.scheduler.flow;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.process.scheduler.GeneralDispatchParameter;

/**
 * 
 * @author DougLei
 */
public class FlowDispatchParameter extends GeneralDispatchParameter{
	private Map<String, Object> variableMap;
	
	public FlowDispatchParameter(int procdefId, int procinstId, List<Assignee> assignees, Map<String, Object> variableMap) {
		super.procdefId = procdefId;
		super.procinstId = procinstId;
		super.assignees = assignees;
		this.variableMap = variableMap;
	}
	
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
}
