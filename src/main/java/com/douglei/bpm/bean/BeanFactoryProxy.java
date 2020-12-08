package com.douglei.bpm.bean;

/**
 * 
 * @author DougLei
 */
public class BeanFactoryProxy {
	private BeanFactory beanFactory;

	BeanFactoryProxy(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	/**
	 * 给指定的实例装配属性
	 * @param instance
	 */
	public void autowireField(Object instance) {
		try {
			beanFactory.setFields(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
