package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 未认领
	 */
	UNCLAIM,
	
	/**
	 * 无效
	 */
	INVALID,
	
	/**
	 * 已认领
	 */
	CLAIM,
	
	/**
	 * 办理完成
	 */
	FINISHED;
}
