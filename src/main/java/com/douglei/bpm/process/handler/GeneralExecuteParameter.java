package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.assignee.Assigners;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter implements ExecuteParameter{
	private Task taskInstance;
	protected Assigners assigners;
	
	protected GeneralExecuteParameter() {}
	public GeneralExecuteParameter(Task taskInstance) {
		this(taskInstance, null);
	}
	public GeneralExecuteParameter(Task taskInstance, Assigners assigners) {
		this.taskInstance = taskInstance;
		this.assigners = assigners;
	}
	
	public Integer getProcdefId() {
		return taskInstance.getProcdefId();
	}
	public Integer getProcinstId() {
		return taskInstance.getProcinstId();
	}
	public Task getTaskInstance() {
		return taskInstance;
	}
	public final Assigners getAssigners() {
		return assigners;
	}
}
