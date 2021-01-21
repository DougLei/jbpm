package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import com.douglei.bpm.module.runtime.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;

/**
 * 设置目标调度
 * @author DougLei
 */
public class SettargetDispatchExecutor extends DispatchExecutor {
	private String targetTask; // 目标任务
	protected boolean executeCC; // 是否进行抄送
	
	protected SettargetDispatchExecutor(boolean executeCC) {
		this.executeCC = executeCC;
	}
	public SettargetDispatchExecutor(String targetTask, boolean executeCC) {
		this.targetTask = targetTask;
		this.executeCC = executeCC;
	}

	@Override
	public void execute() throws TaskDispatchException{
		TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity = currentUserTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(targetTask);
		if(executeCC)
			executeCarbonCopy();
		execute(targetTaskMetadataEntity);
	}
	
	/**
	 * 进行调度
	 * @param targetTaskMetadataEntity
	 */
	protected final void execute(TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity) {
		processEngineBeans.getTaskHandleUtil().dispatchByTask(targetTaskMetadataEntity, handleParameter);
	}
}
