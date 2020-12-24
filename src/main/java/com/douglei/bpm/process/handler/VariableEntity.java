package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.runtime.variable.DataType;
import com.douglei.bpm.module.runtime.variable.Scope;

/**
 * 流程变量
 * @author DougLei
 */
public class VariableEntity {
	private String taskinstId; // 关联的taskinstId
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	VariableEntity(String taskinstId, String name, Scope scope, DataType dataType, Object value) {
		this.taskinstId = taskinstId;
		this.name = name;
		this.scope = scope;
		this.dataType = dataType;
		this.value = value;
	}
	
	public String getTaskinstId() {
		return taskinstId;
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
