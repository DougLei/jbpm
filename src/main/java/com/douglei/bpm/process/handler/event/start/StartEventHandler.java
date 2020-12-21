package com.douglei.bpm.process.handler.event.start;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class StartEventHandler extends AbstractTaskHandler implements TaskHandler<StartEventMetadata, StartEventExecuteParameter, StartEventExecuteParameter> {

	@Override
	public ExecutionResult startup(StartEventMetadata startEvent, StartEventExecuteParameter executeParameter) {
//		if(OgnlHandler.getSingleton().getBooleanValue(startEvent.getConditionExpr(), parameter.getStartParameter().getProcessVariableMapHolder().getVariableMap()))
//			return new ExecutionResult("启动失败, 不满足启动条件");
		return complete(startEvent, executeParameter);
	}
	
	@Override
	public ExecutionResult complete(StartEventMetadata startEvent, StartEventExecuteParameter executeParameter) {
		// 创建流程实例
		ProcessInstance processInstance = createProcessInstance(executeParameter.getProcessMetadata(), executeParameter.getStartParameter());
		
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(processInstance.getProcdefId(), processInstance.getProcinstId(), startEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		Map<String, Object> variableMap = variableHandler.startDispatch(processInstance.getProcinstId(), historyTask.getId(), executeParameter.getVariableMapHolder());
		taskScheduler.dispatch(startEvent, 
				new TaskDispatchParameter(
						processInstance.getProcinstId(), 
						variableMap,
						executeParameter.getProcessMetadata(),
						assignerFactory.create(executeParameter.getStartParameter().getStartUserId())));
		return new ExecutionResult(processInstance);
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata processMetadata, StartParameter startParameter) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setProcinstId(UUID.randomUUID().toString());
		instance.setTitle(new TitleExpression(processMetadata.getTitle()).getTitle(startParameter.getVariableMapHolder().getVariableMap()));
		instance.setBusinessId(startParameter.getBusinessId());
		instance.setPageId(processMetadata.getPageID());
		instance.setStartUserId(startParameter.getStartUserId());
		instance.setStartTime(new Date());
		instance.setTenantId(startParameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
	
	@Override
	public Type getType() {
		return Type.START_EVENT;
	}
}
