package com.douglei.bpm.module.components.command.context;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.transaction.TransactionHandler;

/**
 * 
 * @author DougLei
 */
public class Context {
	private static final ThreadLocal<CommandContext> COMMAND_CONTEXT = new ThreadLocal<CommandContext>();
	
	@SuppressWarnings("rawtypes")
	public static void initialize(Command command, ProcessEngine processEngine) {
		CommandContext context = COMMAND_CONTEXT.get();
		if(context == null) {
			context = new CommandContext(processEngine);
			COMMAND_CONTEXT.set(context);
		}
		context.pushCommand(command);
	}
	public static void clear() {
		if(COMMAND_CONTEXT.get().popCommand())
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
	 * 启用事物处理器
	 * @return
	 */
	public static TransactionHandler enableTransactionHandler() {
		return COMMAND_CONTEXT.get().enableTransactionHandler();
	}
}
