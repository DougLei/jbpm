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
	private String conditionExpression;
	
	public FlowMetadata(String id, String name, String source, String target, int order, String conditionExpression) {
		super(id, name);
		this.source = source;
		this.target = target;
		this.order = order;
		this.conditionExpression = conditionExpression;
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
	public String getConditionExpression() {
		return conditionExpression;
	}

	@Override
	public Type getType() {
		return Type.FLOW;
	}
}
