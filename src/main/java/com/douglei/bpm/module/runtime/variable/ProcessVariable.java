package com.douglei.bpm.module.runtime.variable;

/**
 * 流程变量
 * @author DougLei
 */
public class ProcessVariable {
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	ProcessVariable(String name, Scope scope, DataType dataType, Object value) {
		this.name = name;
		this.scope = scope;
		this.dataType = dataType;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public Scope getScope() {
		return scope;
	}
	public DataType getDataType() {
		return dataType;
	}
	public Object getValue() {
		return value;
	}
}
