package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.task.assignee.Assigners;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.module.runtime.variable.VariableEntityMapHolder;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.TaskHandler;
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
		return execute(startEvent, executeParameter);
	}
	
	@Override
	public ExecutionResult execute(StartEventMetadata startEvent, StartEventExecuteParameter executeParameter) {
		// 创建流程实例
		ProcessInstance processInstance = createProcessInstance(executeParameter.getProcessMetadata(), executeParameter.getStartParameter());
		
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(processInstance.getProcdefId(), processInstance.getId(), startEvent);
		historyTask.setEndTime(historyTask.getStartTime());
		SessionContext.getTableSession().save(historyTask);
		
		saveRuntimeVariables(executeParameter.getVariableEntityMapHolder(), processInstance);
		saveHistoryVariables(executeParameter.getVariableEntityMapHolder(), historyTask);
		
		taskScheduler.dispatch(startEvent, 
				new TaskDispatchParameter(processInstance.getProcdefId(), processInstance.getId(), 
						new Assigners(assignerBuilder.build(executeParameter.getStartParameter().getStartUserId())), executeParameter.getVariableEntityMapHolder().getVariableMap()));
		return new ExecutionResult(processInstance);
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata processMetadata, StartParameter startParameter) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setTitle(new TitleExpression(processMetadata.getTitle()).getTitle(startParameter.getVariableEntityMapHolder().getVariableMap()));
		instance.setBusinessId(startParameter.getBusinessId());
		instance.setPageId(processMetadata.getPageID());
		instance.setStartUserId(startParameter.getStartUserId());
		instance.setStartTime(new Date());
		instance.setTenantId(startParameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
	
	// 保存运行变量, 即GLOBAL变量
	private void saveRuntimeVariables(VariableEntityMapHolder processVariableMapHolder, ProcessInstance processInstance) {
		if(!processVariableMapHolder.existsGlobalVariableMap()) 
			return;
		
		List<Variable> variables = new ArrayList<Variable>(processVariableMapHolder.getGlobalVariableMap().size());
		processVariableMapHolder.getGlobalVariableMap().values().forEach(processVariable -> {
			variables.add(new Variable(processInstance.getProcdefId(), processInstance.getId(), null, processVariable));
		});
		SessionContext.getTableSession().save(variables);
	}

	// 保存历史变量, 即LOCAL变量
	private void saveHistoryVariables(VariableEntityMapHolder processVariableMapHolder, HistoryTask historyTask) {
		if(!processVariableMapHolder.existsLocalVariableMap()) 
			return;
		
		List<HistoryVariable> variables = new ArrayList<HistoryVariable>(processVariableMapHolder.getLocalVariableMap().size());
		processVariableMapHolder.getLocalVariableMap().values().forEach(processVariable -> {
			variables.add(new HistoryVariable(historyTask.getProcdefId(), historyTask.getProcinstId(), historyTask.getId(), processVariable));
		});
		SessionContext.getTableSession().save(variables);
	}
	
	@Override
	public Type getType() {
		return Type.START_EVENT;
	}
}
