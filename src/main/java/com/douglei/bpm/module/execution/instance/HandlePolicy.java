package com.douglei.bpm.module.execution.instance;

/**
 * 流程实例的处理策略
 * @author DougLei
 */
public enum HandlePolicy {
	
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
