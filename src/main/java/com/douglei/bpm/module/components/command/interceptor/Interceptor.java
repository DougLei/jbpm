package com.douglei.bpm.module.components.command.interceptor;

import com.douglei.bpm.bean.MultiInstance;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Interceptor {
	protected Interceptor next;
	
	public final void setNext(Interceptor interceptor) {
		this.next = interceptor;
	}
	
	/**
	 * 拦截器的顺序, 用以排序
	 * @return
	 */
	public int getOrder() {
		return 100;
	}
	
	/**
	 * 拦截器执行方法
	 * @param command
	 * @return
	 */
	public abstract <T> ExecutionResult<T> execute(Command<T> command);
}
