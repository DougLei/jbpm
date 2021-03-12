package com.douglei.bpm.module.runtime.variable;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public class ObjectValue implements Serializable{
	private Object value;

	public ObjectValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
}
