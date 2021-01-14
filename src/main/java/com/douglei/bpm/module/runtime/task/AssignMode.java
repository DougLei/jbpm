package com.douglei.bpm.module.runtime.task;

/**
 * 指派的模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 静态的办理人(静态指派)
	 */
	STATIC,
	
	/**
	 * 指派的办理人(动态指派)
	 */
	ASSIGNED,
	
	/**
	 * 委托的办理人(委托)
	 */
	DELEGATED,
	
	/**
	 * 移交的办理人(转办)
	 */
	TRANSFERRED;
}
