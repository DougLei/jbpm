package com.douglei.bpm.process.metadata.flow;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class FlowMetadata extends ProcessNodeMetadata{
	private String source;
	private String target;
	private int order;
	private String conditionExpr;
	
	public FlowMetadata(String id, String name, String source, String target, int order, String conditionExpr) {
		super(id, name);
		this.source = source;
		this.target = target;
		this.order = order;
		this.conditionExpr = conditionExpr;
	}
	
	public String getSource() {
		return source;
	}
	public String getTarget() {
		return target;
	}
	public int getOrder() {
		return order;
	}
	public String getConditionExpr() {
		return conditionExpr;
	}

	@Override
	public Type getType() {
		return Type.FLOW;
	}
}
