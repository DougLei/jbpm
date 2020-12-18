package com.douglei.bpm.process.handler.event.end;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.metadata.node.event.EndEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class EndEventHandler extends AbstractTaskHandler implements TaskHandler<EndEventMetadata, TaskDispatchParameter, TaskDispatchParameter> {

	@Override
	public ExecutionResult startup(EndEventMetadata endEvent, TaskDispatchParameter executeParameter) {
		return execute(endEvent, executeParameter);
	}

	@Override
	public ExecutionResult execute(EndEventMetadata endEvent, TaskDispatchParameter executeParameter) {
		// 创建流程任务(因为是结束事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(executeParameter.getProcdefId(), executeParameter.getProcinstId(), endEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		// 将实例保存到历史
		
		
		// 将变量保存到历史表
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
