package com.douglei.bpm.module.components.command;

/**
 * 
 * @author DougLei
 */
public interface Command<T> {

	/**
	 * 执行方法
	 * @return
	 */
	T execute();
	
	/**
	 * 是否需要自动装配, 默认为true
	 * @return
	 */
	default boolean autowired() {
		return true;
	}
}
