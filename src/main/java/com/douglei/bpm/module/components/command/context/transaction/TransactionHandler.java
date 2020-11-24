package com.douglei.bpm.module.components.command.context.transaction;

import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.CommandExecuteException;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.orm.sessionfactory.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class TransactionHandler {
	private SessionContext sessionContext;
	
	public TransactionHandler(SessionFactory sessionFactory) {
		this.sessionContext = new SessionContext(sessionFactory);
	}
	
	public SessionContext getSessionContext() {
		return sessionContext;
	}
	
	/**
	 * 开启Session
	 * @param config
	 */
	public void openSession(TransactionConfig config) {
		switch(config.propagationBehavior()) {
			case REQUIRED:
				SessionWrapper sessionWrapper_REQUIRED = sessionContext.existsSessionWrapper();
				if(sessionWrapper_REQUIRED == null) {
					sessionContext.openSession(true, config.transactionIsolationLevel());
				}else {
					Session session = sessionWrapper_REQUIRED.getSession();
					if(!session.isBeginTransaction())
						session.beginTransaction();
					sessionWrapper_REQUIRED.increment(config.transactionIsolationLevel());
				}
				break;
			case REQUIRED_NEW:
				sessionContext.openSession(true, config.transactionIsolationLevel());
				break;
			case SUPPORTS:
				SessionWrapper sessionWrapper_SUPPORTS = sessionContext.existsSessionWrapper();
				if(sessionWrapper_SUPPORTS == null) {
					sessionContext.openSession(false, config.transactionIsolationLevel());
				}else {
					sessionWrapper_SUPPORTS.increment(config.transactionIsolationLevel());
				}
				break;
		}
	}
	
	/**
	 * 提交
	 * @return
	 * @throws Throwable 
	 */
	public void commit() throws Throwable {
		SessionWrapper sessionWrapper = sessionContext.getSessionWrapper();
		if(sessionWrapper.readyCommit()) {
			if(sessionWrapper.getTransactionExecuteMode() == SessionWrapper.ROLLBACK) {
				sessionWrapper.getSession().rollback();
			}else {
				sessionWrapper.getSession().commit();
			}
		}
	}
	/**
	 * (遇异常)回滚
	 * @param command
	 * @param t
	 * @throws CommandExecuteException
	 */
	@SuppressWarnings("rawtypes")
	public void rollback(Command command, Throwable t) throws CommandExecuteException{
		SessionWrapper sessionWrapper = sessionContext.getSessionWrapper();
		sessionWrapper.addException(new CommandExecuteException(command, t));
		if(sessionWrapper.ready()) {
			if(sessionWrapper.getTransactionExecuteMode() == SessionWrapper.COMMIT) {
				sessionWrapper.getSession().commit();
			}else {
				sessionWrapper.getSession().rollback();
			}
			sessionWrapper.throwExceptions();
		}
	}
	/**
	 * 关闭
	 */
	public void close() {
		SessionWrapper sessionWrapper = sessionContext.getSessionWrapper();
		if(sessionWrapper.ready()) {
			sessionWrapper = sessionContext.popSessionWrapper();
			sessionWrapper.close();
		}else {
			sessionWrapper.decrement();
		}
	}
}
