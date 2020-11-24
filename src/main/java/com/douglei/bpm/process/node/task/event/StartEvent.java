package com.douglei.bpm.process.node.task.event;

import com.douglei.bpm.process.node.task.Task;

/**
 * 
 * @author DougLei
 */
public class StartEvent extends Task {
	private String conditionExpr; // 启动条件表达式
	
	public StartEvent(String id, String name, String conditionExpr) {
		super(id, name);
		this.conditionExpr = conditionExpr;
	}
}