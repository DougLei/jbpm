package com.douglei.bpm.core.process.executer.flow;

import com.douglei.bpm.core.process.executer.Node;

/**
 * 
 * @author DougLei
 */
public class Flow extends Node{
	private String source;
	private String target;
	private int order;
	private String conditionExpr;
	public int getOrder() {
		return order;
	}
}
