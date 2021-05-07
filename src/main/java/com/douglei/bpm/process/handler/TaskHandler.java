package com.douglei.bpm.process.handler;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class TaskHandler<TM extends TaskMetadata, HP extends AbstractHandleParameter> extends AbstractTaskHandler {
	protected TaskMetadataEntity<TM> currentTaskMetadataEntity; // 当前任务元数据实体实例
	protected HP handleParameter; // 办理参数
	protected ProcessEngineBeans processEngineBeans; 
	
	/**
	 * 初始化属性
	 * @param currentTaskMetadataEntity
	 * @param handleParameter
	 * @param processEngineBeans
	 */
	final void setParameters(TaskMetadataEntity<TM> currentTaskMetadataEntity, HP handleParameter, ProcessEngineBeans processEngineBeans) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.processEngineBeans = processEngineBeans;
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
	 * @param createBranch 当前任务是否会创建分支
	 * @param parentTaskinstId 当前任务关联的父任务实例id
	 * @return
	 */
	private Task createTask(boolean createBranch, String parentTaskinstId) {
		Task task = new Task(
				handleParameter.getProcessMetadata().getId(),
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				handleParameter.getTaskEntityHandler().getPreviousTaskKey(),
				currentTaskMetadataEntity.getTaskMetadata());
		
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(task, createBranch));
		return task;
	}
	
	/**
	 * 创建历史任务
	 * @return
	 */
	protected final HistoryTask createHistoryTask() {
		if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch())
			return createHistoryTask(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getTaskinstId());
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
				handleParameter.getTaskEntityHandler().getPreviousTaskKey(),
				currentTaskMetadataEntity.getTaskMetadata());
		
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(historyTask));
		return historyTask;
	}
	
	/**
	 * 启动任务
	 * @return 
	 */
	public abstract Result startup();
	
	/**
	 * 办理任务
	 * @return 返回对象的success=true时, 其Object属性为boolean类型, 标识当前用户是否可以进行调度操作
	 */
	public abstract Result handle();
}
