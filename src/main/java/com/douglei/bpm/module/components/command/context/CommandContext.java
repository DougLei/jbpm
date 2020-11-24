package com.douglei.bpm.module.components.command.context;

import java.util.Stack;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.transaction.SessionContext;
import com.douglei.bpm.module.components.command.context.transaction.TransactionHandler;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.bpm.process.ProcessHandler;

/**
 * 
 * @author DougLei
 */
@SuppressWarnings("rawtypes")
public class CommandContext {
	private ProcessEngine processEngine;
	private ProcessHandler processHandler;
	private RepositoryModule repositoryModule;
	private RuntimeModule runtimeModule;
	private HistoryModule historyModule;
	public CommandContext(ProcessEngine processEngine) {
		this.processEngine = processEngine;
		this.processHandler = processEngine.getProcessHandler();
		this.repositoryModule = processEngine.getRepositoryModule();
		this.runtimeModule = processEngine.getRuntimeModule();
		this.historyModule = processEngine.getHistoryModule();
	}
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	public ProcessHandler getProcessHandler() {
		return processHandler;
	}
	public RepositoryModule getRepositoryModule() {
		return repositoryModule;
	}
	public RuntimeModule getRuntimeModule() {
		return runtimeModule;
	}
	public HistoryModule getHistoryModule() {
		return historyModule;
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
