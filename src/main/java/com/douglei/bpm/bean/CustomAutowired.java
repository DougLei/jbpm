package com.douglei.bpm.bean;

import java.util.Map;

/**
 * 自定义的自动装配
 * @author DougLei
 */
public interface CustomAutowired {
	
	/**
	 * 设置属性
	 * @param beanContainer bean容器
	 */
	void setFields(Map<Class<?>, Object> beanContainer);
}
