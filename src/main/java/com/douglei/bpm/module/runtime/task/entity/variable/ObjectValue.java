package com.douglei.bpm.module.runtime.task.entity.variable;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
class ObjectValue implements Serializable{
	private String clazz;
	private Object value;

	public ObjectValue(Object value) {
		this.clazz = value.getClass().getName();
		this.value = value;
	}

	public String getClazz() {
		return clazz;
	}
	public Object getValue() {
		return value;
	}
}
