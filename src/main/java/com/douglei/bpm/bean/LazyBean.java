package com.douglei.bpm.bean;

/**
 * 
 * @author DougLei
 */
class LazyBean {
	private boolean isTransactionBean;
	private Class<?> key;
	private Class<?> loadClass;
	
	public LazyBean(Bean beanAnnotation, Class<?> loadClass) {
		this.isTransactionBean = beanAnnotation.isTransaction();
		this.key = (beanAnnotation.clazz()==Object.class)?loadClass:beanAnnotation.clazz();
		this.loadClass = loadClass;
	}

	public boolean isTransactionBean() {
		return isTransactionBean;
	}
	public Class<?> getKey() {
		return key;
	}
	public Class<?> getLoadClass() {
		return loadClass;
	}
}
