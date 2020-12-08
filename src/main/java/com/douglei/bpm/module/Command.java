package com.douglei.bpm.module;

/**
 * 
 * @author DougLei
 */
public interface Command<T> {
	
	/**
	 * 命令是否需要装配属性
	 * @return
	 */
	default boolean autowireField() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	T execute();
}
