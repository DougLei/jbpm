package com.douglei.bpm.component.variable;

/**
 * 流程变量
 * @author DougLei
 */
class Variable {
	private String name;
	private Scope scope;
	private DataType dataType;
	private Object value;
	
	public Variable(String name, Object value) {
		this(name, Scope.GLOBAL, value);
	}
	public Variable(String name, Scope scope, Object value) {
		this(name, scope, DataType.getByObjectValue(value), value);
	}
	public Variable(String name, DataType dataType, Object value) {
		this(name, Scope.GLOBAL, dataType, value);
	}
	public Variable(String name, Scope scope, DataType dataType, Object value) {
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
