package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;

/**
 * 标准调度
 * @author DougLei
 */
public class StandardDispatchExecutor extends DispatchExecutor {
	
	@Override
	protected DispatchResult parse() {
		// 对task中的flows进行判断, 选择第一条匹配的flow进行调度
		for(FlowMetadata flow : currentTaskMetadataEntity.getOutputFlows()) {
			if(processEngineBeans.getTaskHandleUtil().flowMatching(flow, handleParameter)) 
				return new DispatchResult(flow.getTarget(), assignedUserIds);
		}
		
		FlowMetadata defaultOutputFlow = currentTaskMetadataEntity.getDefaultOutputFlow();
		if(defaultOutputFlow == null)
			throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到满足条件的OutputFlow");
		return new DispatchResult(defaultOutputFlow.getTarget(), assignedUserIds);
	}
}
