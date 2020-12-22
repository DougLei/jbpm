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
	 * 自动装配指定的实例
	 * @param object
	 */
	public void autowireBean(Object object) {
		try {
			beanFactory.setFields(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
