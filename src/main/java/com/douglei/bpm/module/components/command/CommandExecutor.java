package com.douglei.bpm.module.components.command;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class CommandExecutor {
	private Interceptor first;
	
	public <T> ExecutionResult<T> execute(Command<T> command){
		return first.execute(command);
	}
}
