package com.douglei.bpm.bean.entity;

import com.douglei.bpm.bean.MultiInstance;

/**
 * 
 * @author DougLei
 */
public abstract class BeanEntity {
	protected Class<?> key;
	protected boolean supportMultiInstances;
	
	public BeanEntity(Class<?> beanClass, Class<?> clazz) {
		this.key = beanClass==Object.class?clazz:beanClass;
		this.supportMultiInstances = this.key.getAnnotation(MultiInstance.class) != null;
	}

	public Class<?> getKey() {
		return key;
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
