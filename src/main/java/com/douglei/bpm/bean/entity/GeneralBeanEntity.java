package com.douglei.bpm.bean.entity;

import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
public class GeneralBeanEntity extends BeanEntity{
	private Class<?> instanceClazz;

	public GeneralBeanEntity(Class<?> clazz, Class<?> instanceClazz) {
		super(clazz, instanceClazz);
		this.instanceClazz = instanceClazz;
	}

	@Override
	public Object getInstance() {
		return ConstructorUtil.newInstance(instanceClazz);
	}
}
