package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;

/**
 * 设置目标调度
 * @author DougLei
 */
public class SettargetDispatchExecutor extends DispatchExecutor {
	private String targetTask; // 目标任务
	
	public SettargetDispatchExecutor(String targetTask) {
		this.targetTask = targetTask;
	}

	@Override
	public void execute() throws TaskNotExistsException, TaskDispatchException {
		TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity = currentTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(targetTask);
		setAssignedUsers(assignedUserIds);
		
		// 直接调度到指定的任务
		processEngineBeans.getTaskHandleUtil().notifyListners(currentTaskMetadataEntity.getTaskMetadata(), handleParameter, ActiveTime.TASK_DISPATCH);
		handleParameter.getTaskEntityHandler().dispatch();
		processEngineBeans.getTaskHandleUtil().startup(targetTaskMetadataEntity, handleParameter);
	}
}
