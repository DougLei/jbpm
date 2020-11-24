package com.douglei.bpm.module.components.command.interceptor.impl;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.Context;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Interceptor.class)
public class CommandContextInterceptor extends Interceptor{
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Override
	public int getOrder() {
		return 20;
	}
	
	@Override
	public <T> T execute(Command<T> command) {
		try {
			Context.pushCommand(command, processEngine);
			return next.execute(command);
		} finally {
			Context.clear();
		}
	}
}
