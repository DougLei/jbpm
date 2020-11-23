package com.douglei.bpm.module.components.command.interceptor.impl;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Interceptor.class)
public class CommandExecuteInterceptor extends Interceptor{
	
	@Override
	public int getOrder() {
		return 50;
	}
	
	@Override
	public <T> ExecutionResult<T> execute(Command<T> command) {
		return command.execute();
	}
}
