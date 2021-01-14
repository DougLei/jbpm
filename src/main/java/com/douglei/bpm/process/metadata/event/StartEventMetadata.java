package com.douglei.bpm.process.metadata.event;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventMetadata extends TaskMetadata {
	private String conditionExpression; // 启动条件表达式
	
	public StartEventMetadata(String id, String name, String defaultOutputFlowId, String conditionExpression) {
		super(id, name, defaultOutputFlowId);
		this.conditionExpression = conditionExpression;
	}
	
	/**
	 * 获取启动条件表达式
	 * @return
	 */
	public String getConditionExpression() {
		return conditionExpression;
	}

	@Override
	public Type getType() {
		return Type.START_EVENT;
	}
}
