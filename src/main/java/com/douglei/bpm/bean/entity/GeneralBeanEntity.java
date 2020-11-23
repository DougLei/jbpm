package com.douglei.bpm.bean.entity;

import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
public class GeneralBeanEntity extends BeanEntity{
	private Class<?> clazz;

	public GeneralBeanEntity(Class<?> beanClass, Class<?> clazz) {
		super(beanClass, clazz);
		this.clazz = clazz;
	}

	@Override
	public Object getInstance() {
		return ConstructorUtil.newInstance(clazz);
	}
}
