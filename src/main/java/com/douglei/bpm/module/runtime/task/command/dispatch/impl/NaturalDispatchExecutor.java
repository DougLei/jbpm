package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import com.douglei.bpm.module.runtime.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;

/**
 * 自然调度
 * @author DougLei
 */
public class NaturalDispatchExecutor extends DispatchExecutor {

	@Override
	public void execute() throws TaskDispatchException{
		executeCarbonCopy();
		processEngineBeans.getTaskHandleUtil().dispatch(currentUserTaskMetadataEntity, handleParameter);
	}
}
