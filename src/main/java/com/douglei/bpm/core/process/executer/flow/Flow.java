package com.douglei.bpm.core.process.executer.flow;

import com.douglei.bpm.core.process.executer.Node;
import com.douglei.bpm.core.process.executer.task.Task;

/**
 * 
 * @author DougLei
 */
public class Flow extends Node{
	private int order;
	private String conditionExpr;
	private Task targetTask;
	
	public int getOrder() {
		return order;
	}
}
