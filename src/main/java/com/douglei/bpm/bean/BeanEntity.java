package com.douglei.bpm.bean;

/**
 * 
 * @author DougLei
 */
class BeanEntity {
	private Class<?> key;
	private Class<?> clazz;
	private boolean supportMultiInstance;
	private boolean isTransaction;
	
	public BeanEntity(Bean bean, Class<?> clazz) {
		if(bean.isTransaction()) {
			this.key = clazz;
			this.isTransaction = true;
		}else {
			this.key = (bean.clazz()==Object.class)?clazz:bean.clazz();
			this.supportMultiInstance = bean.supportMultiInstance();
		}
		this.clazz = clazz;
	}

	public Class<?> getKey() {
		return key;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public boolean isSupportMultiInstance() {
		return supportMultiInstance;
	}
	public boolean isTransaction() {
		return isTransaction;
	}
}
