package com.douglei.bpm.module.runtime.task;

/**
 * 指派的模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 固定的办理人
	 */
	FIXED,
	
	/**
	 * 上一环节指派的办理人
	 */
	ASSIGNED,
	
	/**
	 * 委托的办理人
	 */
	DELEGATED,
	
	/**
	 * 移交的办理人(转办)
	 */
	TRANSFERRED,
	
	/**
	 * 申请协助的办理人
	 */
	ASSISTED;
}
