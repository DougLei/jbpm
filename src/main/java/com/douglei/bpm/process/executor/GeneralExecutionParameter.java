package com.douglei.bpm.process.executor;

import java.util.List;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class GeneralExecutionParameter implements ExecutionParameter {
	protected Integer procdefId;
	protected Integer procinstId;
	protected List<Assignee> assignees;

	public Integer getProcdefId() {
		return procdefId;
	}
	public Integer getProcinstId() {
		return procinstId;
	}
	public List<Assignee> getAssignees() {
		return assignees;
	}
}
