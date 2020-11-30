package com.douglei.bpm.module.components.variable;

/**
 * 流程变量
 * @author DougLei
 */
public class VariableEntity {
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	public VariableEntity(String name, Object value) {
		this(name, Scope.GLOBAL, value);
	}
	public VariableEntity(String name, Scope scope, Object value) {
		this(name, scope, DataType.getValueByObject(value), value);
	}
	public VariableEntity(String name, DataType dataType, Object value) {
		this(name, Scope.GLOBAL, dataType, value);
	}
	public VariableEntity(String name, Scope scope, DataType dataType, Object value) {
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
