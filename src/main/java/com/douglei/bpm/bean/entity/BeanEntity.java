package com.douglei.bpm.bean.entity;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * 
 * @author DougLei
 */
public abstract class BeanEntity {
	protected Class<?> clazz;
	protected boolean supportMultiInstances;
	
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
	
	/**
	 * 获取实例
	 * @return
	 */
	public abstract Object getInstance();
}
