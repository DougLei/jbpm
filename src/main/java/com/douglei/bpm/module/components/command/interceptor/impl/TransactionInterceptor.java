package com.douglei.bpm.module.components.command.interceptor.impl;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.Context;
import com.douglei.bpm.module.components.command.context.transaction.TransactionConfig;
import com.douglei.bpm.module.components.command.context.transaction.TransactionContext;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Interceptor.class)
public class TransactionInterceptor extends Interceptor {
	
	@Override
	public int getOrder() {
		return 30;
	}
	
	@Override
	public <T> T execute(Command<T> command) {
		TransactionConfig config = command.getTransactionConfig();
		if(config == null)
			return next.execute(command);
		
		TransactionContext transactionContext = Context.getTransactionContext();
		try {
			transactionContext.openSession(config);
			T t = next.execute(command);
			transactionContext.commit();
			return t;
		} catch (Throwable t) {
			transactionContext.rollback(command, t);
			return null;
		} finally {
			transactionContext.close();
		}
	}
}
