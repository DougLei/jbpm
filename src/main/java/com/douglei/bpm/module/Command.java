package com.douglei.bpm.module;

/**
 * 
 * @author DougLei
 */
public interface Command {

	/**
	 * 是否需要自动装配, 默认为true
	 * @return
	 */
	default boolean autowiredRequired() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	ExecutionResult execute();
}
