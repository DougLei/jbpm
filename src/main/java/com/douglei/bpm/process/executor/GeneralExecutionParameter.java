package com.douglei.bpm.process.executor;

import java.util.List;

import com.douglei.bpm.module.runtime.task.entity.Assignee;

/**
 * 
 * @author DougLei
 */
public class GeneralExecutionParameter implements ExecutionParameter {
	protected List<Assignee> assignees;

	public GeneralExecutionParameter(List<Assignee> assignees) {
		this.assignees = assignees;
	}
	
	
}
