package com.douglei.bpm.module.components.command.context;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.module.components.command.context.transaction.TransactionContext;

/**
 * 
 * @author DougLei
 */
public class CommandContext {
	private ProcessEngine processEngine;
	private TransactionContext transactionContext;
	
	public CommandContext(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	public TransactionContext getTransactionContext() {
		if(transactionContext == null)
			transactionContext = new TransactionContext(processEngine.getSessionFactory());
		return transactionContext;
	}
}
