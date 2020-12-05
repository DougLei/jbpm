package com.douglei.bpm.process.executor.flow;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.process.executor.GeneralExecutionParameter;
import com.douglei.bpm.process.executor.ExecutionParameter;

/**
 * 
 * @author DougLei
 */
public class FlowExecutionParameter implements ExecutionParameter {
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

	// 构建通用的任务执行参数
	public GeneralExecutionParameter buildGeneralTaskExecutionParameter() {
		// TODO Auto-generated method stub
		
		
		
		return null;
	}
}
