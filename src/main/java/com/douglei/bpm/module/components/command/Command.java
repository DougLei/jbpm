package com.douglei.bpm.module.components.command;

import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.components.command.context.transaction.TransactionConfig;

/**
 * 
 * @author DougLei
 */
public interface Command<T> {
	
	/**
	 * 事物配置, 返回null标识不需要事物, 即不需要和数据库交互
	 * @return
	 */
	default TransactionConfig getTransactionConfig() {
		return TransactionConfig.DEFAULT_INSTANCE;
	}
	
	/**
	 * 执行
	 * @param commandContext
	 * @return
	 */
	T execute(CommandContext commandContext);
}
