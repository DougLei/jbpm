package com.douglei.bpm.module.runtime.variable;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
class ObjectValue implements Serializable{
	private Object value;

	public ObjectValue(Object value) {
		if(!(value instanceof Serializable))
			throw new IllegalArgumentException("["+value.getClass().getName()+"]类没有实现["+Serializable.class.getName()+"]接口, 无法进行序列化操作");
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
}
