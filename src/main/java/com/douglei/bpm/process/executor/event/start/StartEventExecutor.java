package com.douglei.bpm.process.executor.event.start;

import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.module.runtime.task.entity.variable.Variable;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class StartEventExecutor extends Executor<StartEventMetadata, StartEventExecutionParameter> {
	
	@Override
	public ExecutionResult<ProcessInstance> execute(StartEventMetadata startEvent, StartEventExecutionParameter parameter) {
		/*
		 * 1. 判断能否启动
		 * 2. 创建流程实例
		 * 3. 创建启动的历史任务
		 * 4. 保存流程变量; 全局变量保存到运行表, 本地变量保存到历史表,  瞬时变量丢弃
		 * 5. 执行流处理; 需要传入所有相关的流程变量
		 * 
		 */
		// TODO 启动条件
//		if(起始事件判断当前的启动条件不满足)
//			return new ExecutionResult<ProcessRuntimeInstance>("不能启动", "");
		
		// 创建流程实例
		ProcessInstance processInstance = createProcessInstance(parameter.getProcessMetadata(), parameter.getStartParameter());
		
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(processInstance.getProcdefId(), processInstance.getId(), startEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		// 全局变量保存到运行表
		List<Variable> variables = parameter.getGlobalVariables(processInstance.getProcdefId(), processInstance.getId());
		if(variables != null)
			SessionContext.getTableSession().save(variables);
		
		// 本地变量保存到历史表
		List<HistoryVariable>  historyVariables = parameter.getLocalVariables(processInstance.getProcdefId(), processInstance.getId(), historyTask.getId());
		if(historyVariables != null)
			SessionContext.getTableSession().save(historyVariables);
		
		if(executeFlow(startEvent, parameter.buildFlowExecutionParameter()))
			return new ExecutionResult<ProcessInstance>(processInstance);
		return new ExecutionResult<ProcessInstance>("执行["+startEvent.getName()+"]任务后, 未能匹配到合适的Flow, 使流程无法正常流转, 请联系流程管理员检查["+parameter.getProcessMetadata().getName()+"]的配置");
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata processMetadata, StartParameter startParameter) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setTitle(processMetadata.getTitle(startParameter.getProcessVariables().getVariableMap()));
		instance.setBusinessId(startParameter.getBusinessId());
		instance.setPageId(processMetadata.getPageID());
		instance.setStartUserId(startParameter.getStartUserId());
		instance.setStartTime(new Date());
		instance.setTenantId(startParameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.START_EVENT;
	}
}
