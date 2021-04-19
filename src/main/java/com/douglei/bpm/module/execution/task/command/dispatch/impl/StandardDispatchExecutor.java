package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;

/**
 * 标准调度
 * @author DougLei
 */
public class StandardDispatchExecutor extends DispatchExecutor {
	
	@Override
	public void execute() throws TaskNotExistsException, TaskDispatchException {
		setAssignedUsers(assignedUserIds);
		processEngineBeans.getTaskHandleUtil().dispatch(currentTaskMetadataEntity, handleParameter);
	}
}
