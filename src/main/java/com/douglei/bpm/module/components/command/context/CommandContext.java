package com.douglei.bpm.module.components.command.context;

import java.util.Stack;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.transaction.SessionContext;
import com.douglei.bpm.module.components.command.context.transaction.TransactionHandler;

/**
 * 
 * @author DougLei
 */
@SuppressWarnings("rawtypes")
public class CommandContext {
	private ProcessEngine processEngine;
	public CommandContext(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	
	
	private Stack<Command> commandStack = new Stack<Command>();
	public void pushCommand(Command command) {
		commandStack.push(command);
	}
	public boolean popCommand() {
		commandStack.pop();
		return commandStack.isEmpty();
	}
	
	
	private TransactionHandler transactionHandler;
	TransactionHandler enableTransactionHandler() {
		if(transactionHandler == null)
			transactionHandler = new TransactionHandler(processEngine.getSessionFactory());
		return transactionHandler;
	}
	public SessionContext getSessionContext() {
		if(transactionHandler == null)
			throw new NullPointerException("当前命令不存在可用的seesion实例");
		return transactionHandler.getSessionContext();
	}
}
