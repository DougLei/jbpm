package com.douglei.bpm.module.components.command.context;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.module.components.command.context.transaction.TransactionContext;

/**
 * 
 * @author DougLei
 */
public class Context {
	private static final ThreadLocal<CommandContext> COMMAND_CONTEXT = new ThreadLocal<CommandContext>();
	
	public static void initialize(ProcessEngine processEngine) {
		COMMAND_CONTEXT.set(new CommandContext(processEngine));
	}
	public static void clear() {
		COMMAND_CONTEXT.remove();
	}
	
	/**
	 * 获取命令上下文
	 * @return
	 */
	public static CommandContext getCommandContext() {
		return COMMAND_CONTEXT.get();
	}
	
	/**
	 * 获取事物上下文
	 * @return
	 */
	public static TransactionContext getTransactionContext() {
		return COMMAND_CONTEXT.get().getTransactionContext();
	}
}
