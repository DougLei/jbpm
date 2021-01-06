package com.douglei.bpm.module.runtime.instance;

/**
 * 流程实例状态
 * @author DougLei
 */
public enum ProcessInstanceState {
	
	/**
	 * 活动中
	 */
	ACTIVE,
	
	/**
	 * 挂起
	 */
	SUSPENDED,
	
	/**
	 * 终止
	 */
	TERMINATED,
	
	/**
	 * 结束
	 */
	FINISHED,
	
	/**
	 * 错误
	 */
	ERROR;
}
