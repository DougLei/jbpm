package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.execution.task.runtime.Task;

/**
 * 
 * @author DougLei
 */
public class TaskEntity {
	private Task task;
	private boolean createBranch; // 是否创建分支
	
	public TaskEntity(Task task) {
		this.task = task;
	}
	public TaskEntity(Task task, boolean createBranch) {
		this.task = task;
		this.createBranch = createBranch;
	}
	
	public Task getTask() {
		return task;
	}
	public boolean isCreateBranch() {
		return createBranch;
	}
}
