package com.douglei.bpm.module.runtime.task.entity.variable;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
class ObjectValue implements Serializable{
	private Class<?> clazz;
	private Object value;

	public ObjectValue(Object value) {
		this.clazz = value.getClass();
		this.value = value;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	public Object getValue() {
		return value;
	}
}
