package com.douglei.bpm.module.runtime.task;

import java.util.Arrays;

import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class TaskInstance {
	private ProcessMetadata processMetadata;
	private TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity;
	private Task task;
	
	TaskInstance(int taskId, ProcessMappingContainer container) {
		task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where id=?", Arrays.asList(taskId));
		if(task == null)
			throw new TaskHandleException("不存在id为["+taskId+"]的任务");
		processMetadata = container.getProcess(task.getProcdefId());
		taskMetadataEntity = processMetadata.getTaskMetadataEntity(task.getKey());
	}
	
	TaskInstance(String taskinstId, ProcessMappingContainer container) {
		task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", Arrays.asList(taskinstId));
		if(task == null)
			throw new TaskHandleException("不存在taskinst_id为["+taskinstId+"]的任务");
		processMetadata = container.getProcess(task.getProcdefId());
		taskMetadataEntity = processMetadata.getTaskMetadataEntity(task.getKey());
	}

	/**
	 * 获取流程元数据实例
	 * @return
	 */
	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	/**
	 * 获取任务元数据实例
	 * @return
	 */
	public TaskMetadataEntity<? extends TaskMetadata> getTaskMetadataEntity() {
		return taskMetadataEntity;
	}
	/**
	 * 获取任务实例
	 * @return
	 */
	public Task getTask() {
		return task;
	}
	
	/**
	 * 获取配置的任务name
	 * @return
	 */
	public String getName() {
		return taskMetadataEntity.getTaskMetadata().getName();
	}
	/**
	 * 是否是用户任务
	 * @return
	 */
	public boolean isUserTask() {
		return taskMetadataEntity.getTaskMetadata().isUserTask();
	}
}
