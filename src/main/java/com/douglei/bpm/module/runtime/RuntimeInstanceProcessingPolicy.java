package com.douglei.bpm.module.runtime;

/**
 * 针对流程运行实例的处理策略
 * @author DougLei
 */
public enum RuntimeInstanceProcessingPolicy {
	
	/**
	 * 不进行任何处理
	 */
	NONE,
	
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
	TERMINATE;
}
