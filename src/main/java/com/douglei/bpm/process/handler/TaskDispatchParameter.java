package com.douglei.bpm.process.handler;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class TaskDispatchParameter implements ExecuteParameter{
	private Integer procdefId;
	private Integer procinstId;
	private List<Assignee> assignees;
	private Map<String, Object> variableMap;

	public TaskDispatchParameter(int procdefId, int procinstId, List<Assignee> assignees, Map<String, Object> variableMap) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.assignees = assignees;
		this.variableMap = variableMap;
	}
	
	public Integer getProcdefId() {
		return procdefId;
	}
	public Integer getProcinstId() {
		return procinstId;
	}
	public List<Assignee> getAssignees() {
		return assignees;
	}
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
}
