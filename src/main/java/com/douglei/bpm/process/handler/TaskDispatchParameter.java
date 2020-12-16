package com.douglei.bpm.process.handler;

import java.util.Map;

import com.douglei.bpm.module.runtime.task.assignee.Assigners;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter extends GeneralExecuteParameter{
	private int procdefId;
	private int procinstId;
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(int procdefId, int procinstId, Assigners assigners, Map<String, Object> variableMap) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		super.assigners = assigners;
		this.variableMap = variableMap;
	}
	
	@Override
	public Integer getProcdefId() {
		return procdefId;
	}
	@Override
	public Integer getProcinstId() {
		return procinstId;
	}
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
}
