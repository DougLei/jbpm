package com.douglei.bpm.module.runtime.variable.entity;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
class ObjectValue implements Serializable{
	private static final long serialVersionUID = 389368703391470509L;
	
	private Class<?> clazz;
	private Object value;

	public ObjectValue(Object value) {
		if(!(value instanceof Serializable))
			throw new IllegalArgumentException("["+value.getClass().getName()+"]类没有实现["+Serializable.class.getName()+"]接口, 无法进行序列化操作");
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
