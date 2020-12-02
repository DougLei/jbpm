package com.douglei.bpm.process.metadata.node.event;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventMetadata extends TaskNodeMetadata {
	private String conditionExpr; // 启动条件表达式
	
	public StartEventMetadata(String id, String name, String conditionExpr) {
		super(id, name);
		this.conditionExpr = conditionExpr;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.START_EVENT;
	}
}
