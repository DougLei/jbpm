package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayHandler extends TaskHandler<UserTaskMetadata, GeneralHandleParameter>{

	@Override
	public ExecutionResult startup() {
		return handle();
	}

	@Override
	public ExecutionResult handle() {
		// 创建流程任务(因为是网关, 所以直接创建历史任务即可)
		HistoryTask historyTask = new HistoryTask(handleParameter.getProcessEntity().getProcessMetadata().getId(), handleParameter.getProcessEntity().getProcinstId(), taskMetadata);
		SessionContext.getTableSession().save(historyTask);
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
