package com.douglei.bpm.process.handler.components.variable;

import com.douglei.bpm.module.runtime.variable.DataType;
import com.douglei.bpm.module.runtime.variable.Scope;

/**
 * 流程变量
 * @author DougLei
 */
public class VariableEntity {
	private Integer taskId; // 关联的taskId
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	VariableEntity(Integer taskId, String name, Scope scope, DataType dataType, Object value) {
		this.taskId = taskId;
		this.name = name;
		this.scope = scope;
		this.dataType = dataType;
		this.value = value;
	}
	
	public Integer getTaskId() {
		return taskId;
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
