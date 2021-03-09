package com.douglei.bpm.process.mapping.metadata;

import com.douglei.bpm.ProcessEngineException;

/**
 * 任务不存在异常
 * @author DougLei
 */
public class TaskNotExistsException extends ProcessEngineException {

	public TaskNotExistsException(String taskId) {
		super("不存在id为["+taskId+"]的任务");
	}
}
