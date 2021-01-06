package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.module.runtime.task.Task;

/**
 * 并行任务的join结果
 * @author DougLei
 */
public class ParallelTaskJoinResult {
	private boolean success;
	private Task joinTask;

	/**
	 * 是否合并成功
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 获取进行join的任务实例
	 * @return
	 */
	public Task getJoinTask() {
		return joinTask;
	} 
}
