package com.douglei.bpm.process.metadata.node.task.event;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventMetadata extends TaskMetadata {
	private String conditionExpr; // 启动条件表达式
	
	public StartEventMetadata(String id, String name, String conditionExpr) {
		super(id, name);
		this.conditionExpr = conditionExpr;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.START_EVENT;
	}

	@Override
	public boolean isAuto() {
		return true;
	}
}
