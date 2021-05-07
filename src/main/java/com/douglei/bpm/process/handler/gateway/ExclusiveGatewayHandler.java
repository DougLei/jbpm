package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.command.dispatch.impl.StandardDispatchExecutor;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayHandler extends AbstractGatewayHandler{

	@Override
	public Result startup() {
		removeVariables();
		return handle();
	}
	
	@Override
	public Result handle() {
		// 创建流程任务(因为是网关, 所以直接创建历史任务即可)
		HistoryTask historyTask = createHistoryTask();
		SessionContext.getTableSession().save(historyTask);
		
		new StandardDispatchExecutor()
			.setParameters(currentTaskMetadataEntity, handleParameter, processEngineBeans)
			.execute();
		return CANNOT_DISPATCH;
	}
}
