package com.douglei.bpm.process.handler;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter extends GeneralExecuteParameter{
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(int procdefId, int procinstId, List<Assignee> assignees, Map<String, Object> variableMap) {
		super.procdefId = procdefId;
		super.procinstId = procinstId;
		super.assignees = assignees;
		this.variableMap = variableMap;
	}
	
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
}
