package com.douglei.bpm.process;

/**
 * 
 * @author DougLei
 */
public enum Type {
	FLOW("flow"),
	
	START_EVENT("startEvent"),
	END_EVENT("endEvent"),
	
	EXCLUSIVE_GATEWAY("exclusiveGateway"),
	PARALLEL_GATEWAY("parallelGateway"),
	INCLUSIVE_GATEWAY("inclusiveGateway"),
	
	USER_TASK("userTask");
	
	private String name; // 类型名, 也是xml中的元素名
	private Type(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
