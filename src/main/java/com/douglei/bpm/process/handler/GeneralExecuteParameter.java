package com.douglei.bpm.process.handler;

import java.util.List;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter implements ExecuteParameter{
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
