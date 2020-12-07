package com.douglei.bpm.process.scheduler;

import java.util.List;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class GeneralDispatchParameter implements DispatchParameter {
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
