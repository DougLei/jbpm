package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class StartEventHandler extends TaskHandler<StartEventMetadata, StartEventHandleParameter> {

	@Override
	public ExecutionResult startup() {
//		if(OgnlHandler.getSingleton().getBooleanValue(startEvent.getConditionExpr(), parameter.getStartParameter().getProcessVariableMapHolder().getVariableMap()))
//			return new ExecutionResult("启动失败, 不满足启动条件");
		return handle();
	}
	
	@Override
	public ExecutionResult handle() {
		// 创建流程实例
		ProcessInstance processInstance = createProcessInstance();
		
		// 创建流程任务(因为是开始事件, 所以直接创建历史任务即可)
		HistoryTask historyTask = createHistoryTask(null);
		SessionContext.getTableSession().save(historyTask);
		
		// 保存流程变量
		saveVariables(historyTask);
		
		// 进行任务调度
		beanInstances.getTaskHandlerUtil().dispatch(currentTaskMetadataEntity, handleParameter);
		return new ExecutionResult(processInstance);
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance() {
		ProcessMetadata processMetadata = handleParameter.getProcessMetadata();
		
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setProcinstId(handleParameter.getProcessInstanceId());
		instance.setTitle(new TitleParser(processMetadata.getTitle(), handleParameter.getVariableEntities().getVariableMap()).getTitle());
		instance.setBusinessId(handleParameter.getBusinessId());
		instance.setPageId(processMetadata.getPageID());
		instance.setStartUserId(handleParameter.getUserId());
		instance.setStartTime(handleParameter.getCurrentDate());
		instance.setTenantId(handleParameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
	
	// 保存流程变量
	private void saveVariables(HistoryTask startEvent) {
		VariableEntities variableEntities = handleParameter.getVariableEntities();
		
		// 保存global范围的变量到运行表
		if(variableEntities.existsGlobalVariable()) {
			List<Variable> variables = new ArrayList<Variable>(variableEntities.getGlobalVariableMap().size());
			variableEntities.getGlobalVariableMap().values().forEach(processVariable -> {
				variables.add(new Variable(startEvent.getProcinstId(), null, processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
		
		// 保存local范围的变量到历史表
		if(variableEntities.existsLocalVariable()) {
			List<HistoryVariable> variables = new ArrayList<HistoryVariable>(variableEntities.getLocalVariableMap().size());
			variableEntities.getLocalVariableMap().values().forEach(processVariable -> {
				variables.add(new HistoryVariable(startEvent.getProcinstId(), startEvent.getTaskinstId(), processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
	}
}
