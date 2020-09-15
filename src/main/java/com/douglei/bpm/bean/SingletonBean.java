package com.douglei.bpm.bean;

/**
 * 
 * @author DougLei
 */
public class SingletonBean extends Bean{

	public SingletonBean(Object instance) {
		super.instance = instance;
	}
	
	@Override
	public Object getInstance() {
		return instance;
	}
}
