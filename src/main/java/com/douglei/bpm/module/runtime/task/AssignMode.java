package com.douglei.bpm.module.runtime.task;

/**
 * 指派的模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 固定的办理人
	 */
	FIXED(),
	
	/**
	 * 指派的办理人
	 */
	ASSIGNED(),
	
	/**
	 * 委托的办理人
	 */
	DELEGATED(),
	
	/**
	 * 移交的办理人
	 */
	TRANSFERRED();
}
