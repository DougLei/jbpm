package com.douglei.bpm.module.components.command;

import com.douglei.bpm.module.components.ExecutionResult;

/**
 * 
 * @author DougLei
 */
public interface Command<T> {
	
	/**
	 * 执行
	 * @return
	 */
	ExecutionResult<T> execute();
}
