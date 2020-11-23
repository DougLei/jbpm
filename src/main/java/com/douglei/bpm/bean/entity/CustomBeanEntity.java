package com.douglei.bpm.bean.entity;

/**
 * 
 * @author DougLei
 */
public class CustomBeanEntity extends BeanEntity{
	private Object instance;
	
	public CustomBeanEntity(Class<?> beanClass, Object instance) {
		super(beanClass, instance.getClass());
		this.instance = instance;
	}

	@Override
	public Object getInstance() {
		return instance;
	}
}
