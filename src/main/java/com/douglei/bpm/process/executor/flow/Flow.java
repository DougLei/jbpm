package com.douglei.bpm.process.executor.flow;

import com.douglei.bpm.process.executor.ProcessNode;
import com.douglei.bpm.process.executor.task.Task;

/**
 * 
 * @author DougLei
 */
public class Flow extends ProcessNode{
	private int order;
	private FlowMode mode;
	private String conditionExpr;
	private Task targetTask;
	
	public Flow(String id, String name, int order, FlowMode mode, String conditionExpr) {
		super(id, name);
		this.order = order;
		this.mode = mode;
		this.conditionExpr = conditionExpr;
	}
	
	public void setTargetTask(Task targetTask) {
		this.targetTask = targetTask;
	}

	public int getOrder() {
		return order;
	}
	public FlowMode getMode() {
		return mode;
	}
	public String getConditionExpr() {
		return conditionExpr;
	}
	public Task getTargetTask() {
		return targetTask;
	}
}
