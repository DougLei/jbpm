package com.douglei.bpm.process.mapping.metadata.listener;

import java.io.Serializable;

import com.douglei.bpm.process.handler.AbstractHandleParameter;

/**
 * 监听器
 * <p>
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
public abstract class Listener implements Serializable {
	private ActiveTime activeTime;
	
	protected Listener(ActiveTime activeTime) {
		this.activeTime = activeTime;
	}

	/**
	 * 获取可激活该Listener的ActiveTime
	 * @param target
	 * @return
	 */
	public final ActiveTime getActiveTime() {
		return activeTime;
	}
	
	/**
	 * 
	 * @param handleParameter
	 */
	public abstract void notify(AbstractHandleParameter handleParameter);
}
