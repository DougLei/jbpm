package com.douglei.bpm.process.executor.event.start;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.component.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.module.runtime.task.entity.Variable;
import com.douglei.bpm.process.executor.ProcessExecutor;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=ProcessExecutor.class)
public class StartEventExecutor extends ProcessExecutor<StartEventMetadata, StartEventExecutionParameter> {
	
	@Override
	public Class<StartEventMetadata> getMetadataClass() {
		return StartEventMetadata.class;
	}

	@Override
	public ExecutionResult<ProcessInstance> execute(StartEventMetadata startEvent, StartEventExecutionParameter parameter) {
//		if(起始事件判断当前的启动条件不满足)
//			return new ExecutionResult<ProcessRuntimeInstance>("不能启动", "");
	
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(parameter.getProcessDefinitionId(), parameter.getProcessInstanceId(), startEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		// 保存流程变量
		List<Variable> variables = parameter.getVariables();
		if(variables != null)
			SessionContext.getTableSession().save(variables);
		
		executeFlow(startEvent, parameter.getVariableMap());
		
		
		return null;
	}
	
	
	
	
	// 创建流程实例
//	private ProcessInstance createProcessInstance(ProcessMetadata process) {
//		ProcessInstance instance = new ProcessInstance();
//		instance.setProcdefId(process.getId());
//		instance.setTitle(process.getTitle(parameter.getVariables()));
//		instance.setBusinessId(parameter.getBusinessId());
//		instance.setPageId(process.getPageID());
//		instance.setStartUserId(parameter.getStartUserId());
//		instance.setStartTime(new Date());
//		instance.setTenantId(parameter.getTenantId());
//		
//		SessionContext.getTableSession().save(instance);
//		return instance;
//	}
}
