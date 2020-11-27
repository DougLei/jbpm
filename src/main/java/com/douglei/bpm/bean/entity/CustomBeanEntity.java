package com.douglei.bpm.bean.entity;

/**
 * 
 * @author DougLei
 */
public class CustomBeanEntity extends BeanEntity{
	
	public CustomBeanEntity(Class<?> clazz, Object instance) {
		super(clazz, instance.getClass());
		super.instance = instance;
	}
}
