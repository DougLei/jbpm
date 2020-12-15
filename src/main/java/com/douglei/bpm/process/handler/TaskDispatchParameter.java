package com.douglei.bpm.process.handler;

import java.util.Map;

import com.douglei.bpm.module.runtime.task.assignee.Assigners;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter extends GeneralExecuteParameter{
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(int procdefId, int procinstId, Assigners assigners, Map<String, Object> variableMap) {
		super.procdefId = procdefId;
		super.procinstId = procinstId;
		super.assigners = assigners;
		this.variableMap = variableMap;
	}
	
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
}
