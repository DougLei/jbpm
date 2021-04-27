package com.douglei.bpm.process.api.listener;

/**
 * 流程监听器
 * <p>
 * 
 * 如果需要访问数据库, 可直接调用以下方法:
 * <pre>
 * 	SessionContext.getSqlSession()
 * 	SessionContext.getTableSession()
 * 	SessionContext.getSQLSession()
 *  SessionContext.getSQLQuerySession()
 * </pre>
 * 
 * @author DougLei
 */
public interface Listener {
	
	/**
	 * 
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	/**
	 * 
	 * @return
	 */
	default Scope getScope() {
		return Scope.SINGLETON;
	}
	
	/**
	 * 
	 * @return
	 */
	Executor getExecutor();
	
	/**
	 * 
	 * @return
	 */
	ActiveTime getActiveTime();
}
