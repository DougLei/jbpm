package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceState;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 
 * @author DougLei
 */
public class StartEventHandler extends TaskHandler<StartEventMetadata, StartEventHandleParameter> {

	@Override
	public ExecutionResult startup() {
		String conditionExpression = currentTaskMetadataEntity.getTaskMetadata().getConditionExpression();
		if(conditionExpression != null) {
			Map<String, Object> variableMap = handleParameter.getVariableEntities().getVariableMap();
			if(variableMap == null || !OgnlHandler.getSingleton().getBooleanValue(conditionExpression, variableMap))
				return new ExecutionResult("启动失败, 当前参数不满足[%s]流程的启动条件", "jbpm.process.start.fail.condition.mismatch", handleParameter.getProcessMetadata().getName());
		}
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
		processEngineBeans.getTaskHandleUtil().dispatch(currentTaskMetadataEntity, handleParameter);
		return new ExecutionResult(processInstance);
	}
	
	// 创建流程实例
	private ProcessInstance createProcessInstance() {
		ProcessMetadata processMetadata = handleParameter.getProcessMetadata();
		
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(processMetadata.getId());
		instance.setProcinstId(handleParameter.getProcessInstanceId());
		instance.setTitle(getTitle(processMetadata.getTitle(), handleParameter.getVariableEntities().getVariableMap()));
		instance.setBusinessId(handleParameter.getBusinessId());
		instance.setPageId(processMetadata.getPageID());
		instance.setStartUserId(handleParameter.getUserEntity().getCurrentHandleUser().getUserId());
		instance.setStartTime(handleParameter.getCurrentDate());
		instance.setTenantId(handleParameter.getTenantId());
		instance.setStateInstance(ProcessInstanceState.ACTIVE);
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
	
	// 获取标题
	private String getTitle(String title, Map<String, Object> variableMap) {
		if(variableMap == null || title.indexOf(TitleVariableConstant.PREFIX) == -1)
			return title;
		
		List<String> variableNames = null; // 存储标题中的变量名
		String tempVariableName = null;
		Matcher prefixMatcher = TitleVariableConstant.PREFIX_REGEX_PATTERN.matcher(title);
		Matcher suffixMatcher = TitleVariableConstant.SUFFIX_REGEX_PATTERN.matcher(title);
		while(prefixMatcher.find()) {
			if(!suffixMatcher.find())
				break;
			
			tempVariableName = title.substring(prefixMatcher.start()+2, suffixMatcher.start());
			if(variableNames == null) 
				variableNames = new ArrayList<String>();
			if(variableNames.isEmpty() || !variableNames.contains(tempVariableName)) 
				variableNames.add(tempVariableName);
		}
		
		if(variableNames == null)
			return title;
		
		Object value = null;
		for (String variableName : variableNames) {
			value = OgnlHandler.getSingleton().getObjectValue(variableName, variableMap);;
			if(value != null)
				title = title.replaceAll(TitleVariableConstant.PREFIX_4_REGEX + variableName + TitleVariableConstant.SUFFIX, value.toString());
		}
		return title;
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
