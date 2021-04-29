package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;

/**
 * 标准调度
 * @author DougLei
 */
public class StandardDispatchExecutor extends DispatchExecutor {
	
	@Override
	public void execute() throws TaskNotExistsException, TaskDispatchException {
		setAssignedUsers(assignedUserIds);
		
		// 对task中的flows进行判断, 选择第一条匹配的flow进行调度
		processEngineBeans.getTaskHandleUtil().notifyListners(currentTaskMetadataEntity.getTaskMetadata(), handleParameter, ActiveTime.TASK_DISPATCH);
		for(FlowMetadata flow : currentTaskMetadataEntity.getOutputFlows()) {
			if(processEngineBeans.getTaskHandleUtil().flowMatching(flow, handleParameter)) {
				dispatchByFlow(flow, handleParameter);
				return;
			}
		}
		
		FlowMetadata defaultOutputFlow = currentTaskMetadataEntity.getDefaultOutputFlow();
		if(defaultOutputFlow == null)
			throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到满足条件的OutputFlow");
		dispatchByFlow(defaultOutputFlow, handleParameter);
	}
	
	// 使用指定的flow进行调度
	private void dispatchByFlow(FlowMetadata flowMetadata, AbstractHandleParameter parameter) {
		parameter.getTaskEntityHandler().dispatch();
		processEngineBeans.getTaskHandleUtil().startup(parameter.getProcessMetadata().getTaskMetadataEntity(flowMetadata.getTarget()), parameter);
	}
}
