package com.douglei.bpm.module.runtime.task.command.parameter;

import com.douglei.bpm.module.runtime.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.module.runtime.task.command.dispatch.impl.StandardDispatchExecutor;

/**
 * 
 * @author DougLei
 */
public class DispatchTaskParameter extends GeneralTaskParameter {
	private DispatchExecutor dispatchExecutor;
	
	/**
	 * 设置调度执行器
	 * @param dispatchExecutor
	 */
	public DispatchTaskParameter setDispatchExecutor(DispatchExecutor dispatchExecutor) {
		this.dispatchExecutor = dispatchExecutor;
		return this;
	}
	
	/**
	 * 获取调度执行器
	 * @return
	 */
	public DispatchExecutor getDispatchExecutor() {
		if(dispatchExecutor == null)
			dispatchExecutor = new StandardDispatchExecutor();
		return dispatchExecutor;
	}
}
