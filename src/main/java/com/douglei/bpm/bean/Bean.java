package com.douglei.bpm.bean;

import com.douglei.bpm.bean.annotation.ProcessEngineBean;

/**
 * 
 * @author DougLei
 */
public class Bean {
	private Class<?> clz;
	private ProcessEngineBean bean;
	protected Object instance;
	
	
	public Object getInstance() {
		return instance;
	}
}
