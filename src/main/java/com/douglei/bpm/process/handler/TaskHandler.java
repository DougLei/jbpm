package com.douglei.bpm.process.handler;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.orm.context.SessionContext;

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
	 * @param createBranch 当前任务是否会创建分支
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @return
	 */
	protected final Task createTask(boolean createBranch, String parentTaskinstId) {
		return createTask(createBranch, parentTaskinstId, true);
	}
	/**
	 * 创建任务
	 * @param createBranch 当前任务是否会创建分支
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @param isSave 是否保存创建出的任务
	 * @return
	 */
	protected final Task createTask(boolean createBranch, String parentTaskinstId, boolean isSave) {
		Task task = new Task(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		
		if(isSave)
			SessionContext.getTableSession().save(task);
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(task, createBranch));
		return task;
	}
	
	/**
	 * 创建历史任务
	 * @return
	 */
	protected final Task createHistoryTask() {
		return createHistoryTask(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId());
	}
	/**
	 * 创建历史任务
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @return
	 */
	protected final HistoryTask createHistoryTask(String parentTaskinstId) {
		HistoryTask historyTask = new HistoryTask(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		SessionContext.getTableSession().save(historyTask);
		
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
