package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.module.runtime.task.entity.variable.DataType;
import com.douglei.bpm.module.runtime.task.entity.variable.Scope;

/**
 * 流程变量
 * @author DougLei
 */
public class ProcessVariable {
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	ProcessVariable(String name, Scope scope, Object value) {
		this.name = name;
		this.scope = scope;
		this.dataType = DataType.getByObjectValue(value);
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
