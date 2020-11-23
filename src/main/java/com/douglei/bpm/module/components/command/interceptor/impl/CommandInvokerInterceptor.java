package com.douglei.bpm.module.components.command.interceptor.impl;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.components.command.context.Context;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Interceptor.class)
public class CommandInvokerInterceptor extends Interceptor{
	
	@Override
	public int getOrder() {
		return 50;
	}
	
	@Override
	public <T> T execute(Command<T> command) {
		CommandContext commandContext = Context.getCommandContext();

		return command.execute(commandContext);
	}
}
