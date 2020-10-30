package com.douglei.bpm.process.executer.flow;

import com.douglei.bpm.process.executer.Node;
import com.douglei.bpm.process.executer.task.Task;

/**
 * 
 * @author DougLei
 */
public class Flow extends Node{
	private int order;
	private String conditionExpr;
	private Task targetTask;
	
	public Flow(String id, String name, int order, String conditionExpr) {
		super(id, name);
		this.order = order;
		this.conditionExpr = conditionExpr;
	}
	
	public void setTargetTask(Task targetTask) {
		this.targetTask = targetTask;
	}

	public int getOrder() {
		return order;
	}
	public String getConditionExpr() {
		return conditionExpr;
	}
	public Task getTargetTask() {
		return targetTask;
	}
}
