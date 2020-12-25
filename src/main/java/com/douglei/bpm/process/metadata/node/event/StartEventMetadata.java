package com.douglei.bpm.process.metadata.node.event;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.SingleFlowTaskMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventMetadata extends SingleFlowTaskMetadata {
	private String conditionExpr; // 启动条件表达式
	
	public StartEventMetadata(String id, String name, String conditionExpr) {
		super(id, name);
		this.conditionExpr = conditionExpr;
	}
	
	public String getConditionExpr() {
		return conditionExpr;
	}

	@Override
	public Type getType() {
		return Type.START_EVENT;
	}
}
