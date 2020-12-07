package com.douglei.bpm.process.executor.flow;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

public class FlowExecutionParameter {
	private Map<String, Object> variableMap;
	private List<Assignee> assignees;

	public FlowExecutionParameter(Map<String, Object> variableMap, List<Assignee> assignees) {
		this.variableMap = variableMap;
		this.assignees = assignees;
	}
	
	public Map<String, Object> getVariableMap() {
		return variableMap;
	}
	public List<Assignee> getAssignees() {
		return assignees;
	}
}
