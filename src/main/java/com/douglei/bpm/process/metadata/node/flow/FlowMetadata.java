package com.douglei.bpm.process.metadata.node.flow;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class FlowMetadata extends ProcessNodeMetadata{
	private int order;
	private FlowMode mode;
	private String conditionExpr;
	private TaskMetadata targetTask;
	
	public FlowMetadata(String id, String name, NodeType type, int order, FlowMode mode, String conditionExpr) {
		super(id, name, type);
		this.order = order;
		this.mode = mode;
		this.conditionExpr = conditionExpr;
	}
	
	public void setTargetTask(TaskMetadata targetTask) {
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
	public TaskMetadata getTargetTask() {
		return targetTask;
	}
}
