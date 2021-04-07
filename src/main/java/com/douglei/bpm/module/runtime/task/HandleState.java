package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 无效, 再也无法使用的状态
	 */
	INVALID,
	
	/**
	 * 无效的未认领
	 */
	INVALID_UNCLAIM,
	
	/**
	 * 可竞争的未认领
	 */
	COMPETITIVE_UNCLAIM,
	
	/**
	 * 未认领
	 */
	UNCLAIM,
	
	/**
	 * 已认领
	 */
	CLAIMED,
	
	/**
	 * 办理完成
	 */
	FINISHED;
}
