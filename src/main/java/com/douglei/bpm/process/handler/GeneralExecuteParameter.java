package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.runtime.task.assignee.Assigners;

/**
 * 
 * @author DougLei
 */
public class GeneralExecuteParameter implements ExecuteParameter{
	protected Integer procdefId;
	protected Integer procinstId;
	protected Assigners assigners;
	
	public Integer getProcdefId() {
		return procdefId;
	}
	public Integer getProcinstId() {
		return procinstId;
	}
	public Assigners getAssigners() {
		return assigners;
	}
}
