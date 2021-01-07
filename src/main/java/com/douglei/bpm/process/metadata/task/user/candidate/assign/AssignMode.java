package com.douglei.bpm.process.metadata.task.user.candidate.assign;

/**
 * 指派模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 上一环节指派的办理人
	 */
	ASSIGNED,
	
	/**
	 * 固定的办理人
	 */
	FIXED,
	
	/**
	 * 从流程变量获取的办理人
	 */
	VARIABLE;
}
