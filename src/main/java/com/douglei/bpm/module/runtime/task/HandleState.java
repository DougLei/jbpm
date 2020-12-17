package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 未办理
	 */
	UNHANDLE,
	
	/**
	 * 办理中
	 */
	HANDLING,
	
	/**
	 * 办理完成
	 */
	FINISHED,
	
	/**
	 * 无效
	 */
	INVALID;
}
