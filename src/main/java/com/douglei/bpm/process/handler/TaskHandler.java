package com.douglei.bpm.process.handler;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class TaskHandler<TM extends TaskMetadata, HP extends HandleParameter> extends GeneralTaskHandler {
	protected BeanInstances beanInstances; 
	protected TaskMetadataEntity<TM> currentTaskMetadataEntity; // 当前任务元数据实体实例
	protected HP handleParameter; // 办理参数
	
	// 设置参数
	final void setParameters(BeanInstances beanInstances, TaskMetadataEntity<TM> currentTaskMetadataEntity, HP handleParameter) {
		this.beanInstances = beanInstances;
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
	}
	
	/**
	 * 创建任务
	 * <p>
	 * 不对创建的任务进行保存, 需要调用方保存
	 * @param createBranch 当前任务是否会创建分支
	 * @return
	 */
	protected final Task createTask(boolean createBranch) {
		if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch())
			return createTask(createBranch, handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getTaskinstId());
		return createTask(createBranch, handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId());
	}
	/**
	 * 创建任务
	 * <p>
	 * 不对创建的任务进行保存, 需要调用方保存
	 * @param createBranch 当前任务是否需要创建分支
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @return
	 */
	protected final Task createTask(boolean createBranch, String parentTaskinstId) {
		Task task = new Task(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(task, createBranch));
		return task;
	}
	
	/**
	 * 创建历史任务
	 * <p>
	 * 不对创建的任务进行保存, 需要调用方保存
	 * @return
	 */
	protected final HistoryTask createHistoryTask() {
		if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch())
			return createHistoryTask(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getTaskinstId());
		return createHistoryTask(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId());
	}
	/**
	 * 创建历史任务
	 * <p>
	 * 不对创建的任务进行保存, 需要调用方保存
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @return
	 */
	protected final HistoryTask createHistoryTask(String parentTaskinstId) {
		HistoryTask historyTask = new HistoryTask(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(historyTask));
		return historyTask;
	}
	
	/**
	 * 启动任务
	 * @return
	 */
	public abstract ExecutionResult startup();
	
	/**
	 * 办理任务
	 * @return
	 */
	public abstract ExecutionResult handle();
}
