package com.douglei.bpm.module.components.command.context.transaction;

import java.util.Stack;

import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.CommandExecuteException;
import com.douglei.orm.configuration.environment.datasource.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.orm.sessionfactory.sessions.Session;
import com.douglei.orm.sessionfactory.sessions.session.sql.SQLSession;
import com.douglei.orm.sessionfactory.sessions.session.table.TableSession;
import com.douglei.orm.sessionfactory.sessions.sqlsession.SqlSession;

/**
 * 
 * @author DougLei
 */
public class TransactionContext {
	private SessionFactory sessionFactory;
	private Stack<SessionWrapper> sessionWrappers = new Stack<SessionWrapper>();
	
	public TransactionContext(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 获取Session
	 * @return
	 */
	public Session getSession() {
		return getSessionWrapper().getSession();
	}
	/**
	 * 获取Session
	 * @return
	 */
	public SqlSession getSqlSession() {
		return getSession().getSqlSession();
	}
	/**
	 * 获取Session
	 * @return
	 */
	public TableSession getTableSession() {
		return getSession().getTableSession();
	}
	/**
	 * 获取Session
	 * @return
	 */
	public SQLSession getSQLSession() {
		return getSession().getSQLSession();
	}
	
	/**
	 * 对当前命令的事物进行commit, 会覆盖原本事物要执行的commit或rollback, 可以理解为手动(强制)提交
	 */
	public void executeCommit() {
		getSessionWrapper().setTransactionExecuteMode(SessionWrapper.COMMIT);
	}
	
	/**
	 * 对当前命令的事物进行rollback, 会覆盖原本事物要执行的commit或rollback, 可以理解为手动(强制)回滚
	 */
	public void executeRollback() {
		getSessionWrapper().setTransactionExecuteMode(SessionWrapper.ROLLBACK);
	}
	
	/**
	 * 获取当前的SessionWrapper实例
	 * @return
	 */
	private SessionWrapper getSessionWrapper() {
		if(sessionWrappers.isEmpty()) 
			throw new NullPointerException("不存在可用的seesion实例");
		return sessionWrappers.peek();
	}
	
	/**
	 * 开启Session
	 * @param config
	 */
	public void openSession(TransactionConfig config) {
		switch(config.propagationBehavior()) {
			case REQUIRED:
				SessionWrapper sessionWrapper_REQUIRED = existsSessionWrapper();
				if(sessionWrapper_REQUIRED == null) {
					openSession(true, config.transactionIsolationLevel());
				}else {
					Session session = sessionWrapper_REQUIRED.getSession();
					if(!session.isBeginTransaction())
						session.beginTransaction();
					sessionWrapper_REQUIRED.increment(config.transactionIsolationLevel());
				}
				break;
			case REQUIRED_NEW:
				openSession(true, config.transactionIsolationLevel());
				break;
			case SUPPORTS:
				SessionWrapper sessionWrapper_SUPPORTS = existsSessionWrapper();
				if(sessionWrapper_SUPPORTS == null) {
					openSession(false, config.transactionIsolationLevel());
				}else {
					sessionWrapper_SUPPORTS.increment(config.transactionIsolationLevel());
				}
				break;
		}
	}
	// 判断是否存在SessionWrapper
	private SessionWrapper existsSessionWrapper() {
		if(sessionWrappers.isEmpty()) 
			return null;
		return sessionWrappers.peek();
	}
	// 开启Session
	private void openSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Session session = sessionFactory.openSession(beginTransaction, transactionIsolationLevel);
		SessionWrapper sessionWrapper = new SessionWrapper(session, transactionIsolationLevel);
		sessionWrappers.push(sessionWrapper);
	}
	
	/**
	 * 提交
	 * @return
	 * @throws Throwable 
	 */
	public void commit() throws Throwable {
		SessionWrapper sessionWrapper = getSessionWrapper();
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
	 */
	@SuppressWarnings("rawtypes")
	public void rollback(Command command, Throwable t) {
		SessionWrapper sessionWrapper = getSessionWrapper();
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
		SessionWrapper sessionWrapper = getSessionWrapper();
		if(sessionWrapper.ready()) {
			sessionWrapper = sessionWrappers.pop();
			sessionWrapper.close();
		}else {
			sessionWrapper.decrement();
		}
	}
}
