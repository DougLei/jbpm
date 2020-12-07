package com.douglei.bpm.process.metadata.node.flow;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class FlowMetadata extends ProcessNodeMetadata{
	private int order;
	private String conditionExpr;
	private TaskMetadata targetTask;
	
	public FlowMetadata(String id, String name, int order, String conditionExpr) {
		super(id, name);
		this.order = order;
		this.conditionExpr = conditionExpr;
	}
	
	// 设置目标任务
	public void setTargetTask(TaskMetadata targetTask) {
		this.targetTask = targetTask;
	}

	public int getOrder() {
		return order;
	}
	public TaskMetadata getTargetTask() {
		return targetTask;
	}
	public String getConditionExpr() {
		return conditionExpr;
	}

	@Override
	public NodeType getType() {
		return NodeType.FLOW;
	}
}
