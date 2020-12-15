package com.douglei.bpm.process.handler.event.start;

import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.module.runtime.variable.entity.Variable;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class StartEventHandler extends TaskHandler<StartEventMetadata, StartEventExecuteParameter> {

	@Override
	public ExecutionResult startup(StartEventMetadata startEvent, StartEventExecuteParameter parameter) {
		// TODO 启动条件
//		if(起始事件判断当前的启动条件不满足)
//			return new ExecutionResult<ProcessRuntimeInstance>("不能启动", "");
		
		return execute(startEvent, parameter);
	}
	
	@Override
	public ExecutionResult execute(StartEventMetadata startEvent, StartEventExecuteParameter parameter) {
		// 创建流程实例
		ProcessInstance processInstance = createProcessInstance(parameter.getProcessMetadata(), parameter.getStartParameter());
		
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(processInstance.getProcdefId(), processInstance.getId(), startEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		// 全局变量保存到运行表
		List<Variable> variables = parameter.getRuntimeVariables(processInstance.getProcdefId(), processInstance.getId());
		if(variables != null)
			SessionContext.getTableSession().save(variables);
		
		// 本地变量保存到历史表
		List<HistoryVariable>  historyVariables = parameter.getHistoryVariables(processInstance.getProcdefId(), processInstance.getId(), historyTask.getId());
		if(historyVariables != null)
			SessionContext.getTableSession().save(historyVariables);
		
		taskScheduler.dispatch(startEvent, parameter.buildDispatchParameter(processInstance.getId()));
		return new ExecutionResult(processInstance);
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata processMetadata, StartParameter startParameter) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setTitle(new TitleExpression(processMetadata.getTitle()).getTitle(startParameter.getProcessVariableMapHolder().getVariableMap()));
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
