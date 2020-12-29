package com.douglei.bpm.module.runtime.task;

import java.util.Arrays;

import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class TaskEntity {
	private ProcessMetadata processMetadata;
	private TaskMetadataEntity taskMetadata;
	private Task task;
	
	TaskEntity(int taskId, ProcessContainerProxy container) {
		task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where id=?", Arrays.asList(taskId));
		if(task == null)
			throw new NullPointerException("不存在id为["+taskId+"]的任务");
		processMetadata = container.getProcess(task.getProcdefId());
		taskMetadata = processMetadata.getTask(task.getKey());
	}
	
	TaskEntity(String taskinstId, ProcessContainerProxy container) {
		task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", Arrays.asList(taskinstId));
		if(task == null)
			throw new NullPointerException("不存在taskinst_id为["+taskinstId+"]的任务");
		processMetadata = container.getProcess(task.getProcdefId());
		taskMetadata = processMetadata.getTask(task.getKey());
	}

	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	public TaskMetadata getTaskMetadata() {
		return taskMetadata;
	}
	public String getName() {
		return taskMetadata.getName();
	}
	public boolean supportUserHandling() {
		return taskMetadata.supportUserHandling();
	}
	public Task getTask() {
		return task;
	}
}
