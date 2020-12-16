package com.douglei.bpm.module.runtime.task.assignee;

/**
 * 指派的模式
 * @author DougLei
 */
public enum Mode {
	
	/**
	 * 配置文件中的配置
	 */
	CONFIG,
	
	/**
	 * 上一环节指派
	 */
	ASSIGN,
	
	/**
	 * 委托
	 */
	DELEGATE,
	
	/**
	 * 协助
	 */
	ASSIST;
}
