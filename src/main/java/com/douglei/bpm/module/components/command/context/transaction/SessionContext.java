package com.douglei.bpm.module.components.command.context.transaction;

import java.util.Stack;

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
public class SessionContext {
	private SessionFactory sessionFactory;
	private Stack<SessionWrapper> sessionWrappers = new Stack<SessionWrapper>();
	
	SessionContext(SessionFactory sessionFactory) {
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
	 * 获取SqlSession
	 * @return
	 */
	public SqlSession getSqlSession() {
		return getSession().getSqlSession();
	}
	/**
	 * 获取TableSession
	 * @return
	 */
	public TableSession getTableSession() {
		return getSession().getTableSession();
	}
	/**
	 * 获取SQLSession
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
	
	
	// 获取当前的SessionWrapper实例
	SessionWrapper getSessionWrapper() {
		if(sessionWrappers.isEmpty()) 
			throw new NullPointerException("不存在可用的seesion实例");
		return sessionWrappers.peek();
	}
	// 判断当前是否存在SessionWrapper实例
	SessionWrapper existsSessionWrapper() {
		if(sessionWrappers.isEmpty()) 
			return null;
		return sessionWrappers.peek();
	}
	// 开启Session
	void openSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Session session = sessionFactory.openSession(beginTransaction, transactionIsolationLevel);
		SessionWrapper sessionWrapper = new SessionWrapper(session, transactionIsolationLevel);
		sessionWrappers.push(sessionWrapper);
	}
	// 获取并移除栈顶的session, 调用该方法前, 请务必先调用getSessionWrapper().ready()方法, 判断是否满足了出栈的条件
	SessionWrapper popSessionWrapper() {
		return sessionWrappers.pop();
	}
}
