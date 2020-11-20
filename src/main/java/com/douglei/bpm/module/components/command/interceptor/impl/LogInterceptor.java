package com.douglei.bpm.module.components.command.interceptor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
public class LogInterceptor extends Interceptor{
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
	
	@Override
	public int getOrder() {
		return 10;
	}
	
	@Override
	public <T> ExecutionResult<T> execute(Command<T> command) {
		if(logger.isDebugEnabled()) {
			try {
				logger.debug("开始执行命令: {}", command.getClass().getSimpleName());
				return next.execute(command);
			} finally {
				logger.debug("结束执行命令: {}", command.getClass().getSimpleName());
			}
		}
		return next.execute(command);
	}
}
