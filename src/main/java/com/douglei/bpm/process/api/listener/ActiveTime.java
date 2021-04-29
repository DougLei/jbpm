package com.douglei.bpm.process.api.listener;

/**
 * 
 * @author DougLei
 */
public enum ActiveTime {
	
	/**
	 * 任务开始时
	 */
	TASK_START,
	
	/**
	 * 任务创建时
	 */
	TASK_CREATE,
	
	/**
	 * 任务办理时
	 */
	TASK_HANDLE,
	
	/**
	 * 任务完成时
	 */
	TASK_FINISH;
}
