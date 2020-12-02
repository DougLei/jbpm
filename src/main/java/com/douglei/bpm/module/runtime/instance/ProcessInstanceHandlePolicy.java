package com.douglei.bpm.module.runtime.instance;

/**
 * 流程实例的处理策略
 * @author DougLei
 */
public enum ProcessInstanceHandlePolicy {
	
	/**
	 * 激活
	 */
	ACTIVATE,
	
	/**
	 * 挂起
	 */
	SUSPEND,
	
	/**
	 * 终止
	 */
	TERMINATE,
	
	/**
	 * 删除
	 */
	DELETE;
}
