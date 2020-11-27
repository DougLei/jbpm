package com.douglei.bpm.process;

/**
 * 
 * @author DougLei
 */
public enum NodeType {
	FLOW("flow"),
	
	START_EVENT("startEvent"),
	END_EVENT("endEvent"),
	
	EXCLUSIVE_GATEWAY("exclusiveGateway"),
	PARALLEL_GATEWAY("parallelGateway"),
	INCLUSIVE_GATEWAY("inclusiveGateway"),
	
	USER_TASK("userTask");
	
	private String elementName; // xml中的元素名
	private NodeType(String elementName) {
		this.elementName = elementName;
	}
	
	public String getElementName() {
		return elementName;
	}
	public String getTypeName() { // 获取类型名
		return name();
	}
}
