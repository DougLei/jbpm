package com.douglei.bpm.process.metadata.node.flow;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class FlowMetadata extends ProcessNodeMetadata{
	private int order;
	private String conditionExpr;
	private TaskNodeMetadata targetTask;
	
	public FlowMetadata(String id, String name, NodeType type, int order, String conditionExpr) {
		super(id, name);
		this.order = order;
		this.conditionExpr = conditionExpr;
	}
	
	// 设置目标任务
	public void setTargetTask(TaskNodeMetadata targetTask) {
		this.targetTask = targetTask;
	}

	public int getOrder() {
		return order;
	}
	public String getConditionExpr() {
		return conditionExpr;
	}
	public TaskNodeMetadata getTargetTask() {
		return targetTask;
	}

	@Override
	public NodeType getType() {
		return NodeType.FLOW;
	}
}
