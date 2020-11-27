package com.douglei.bpm.bean.entity;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * 
 * @author DougLei
 */
public abstract class BeanEntity {
	private Class<?> clazz;
	private boolean supportMultiInstances;
	protected Object instance;
	
	public BeanEntity(Class<?> clazz, Class<?> instanceClazz) {
		this.clazz = (clazz==Object.class || clazz==instanceClazz)?instanceClazz:clazz;
		this.supportMultiInstances = this.clazz.getAnnotation(MultiInstance.class) != null;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	public boolean supportMultiInstances() {
		return supportMultiInstances;
	}
	public Object getInstance() {
		return instance;
	}
}
