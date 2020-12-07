package com.douglei.bpm.process.executor.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessVariable;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.variable.Variable;
import com.douglei.bpm.process.executor.ExecutionParameter;
import com.douglei.bpm.process.executor.flow.FlowExecutionParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class StartEventExecutionParameter implements ExecutionParameter {
	private ProcessMetadata processMetadata;
	private StartParameter startParameter;
	
	public StartEventExecutionParameter(ProcessMetadata processMetadata, StartParameter startParameter) {
		this.processMetadata = processMetadata;
		this.startParameter = startParameter;
	}

	// 获取流程实例
	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	
	// 获取启动参数实例
	public StartParameter getStartParameter() {
		return startParameter;
	}
	
	// 获取Global变量集合
	public List<Variable> getGlobalVariables(int processDefinitionId, int processInstanceId){
		if(startParameter.getProcessVariables() == null)
			return null;
		
		Map<String, ProcessVariable> globalVariableMap = startParameter.getProcessVariables().getGlobalVariableMap();
		if(globalVariableMap == null)
			return null;
		
		List<Variable> list = new ArrayList<Variable>(globalVariableMap.size());
		globalVariableMap.values().forEach(processVariable -> {
			list.add(new Variable(processDefinitionId, processInstanceId, null, processVariable));
		});
		return list;
	}
	
	// 获取Local变量集合
	public List<HistoryVariable> getLocalVariables(int processDefinitionId, int processInstanceId, int historyTaskId){
		if(startParameter.getProcessVariables() == null)
			return null;
		
		Map<String, ProcessVariable> localVariableMap = startParameter.getProcessVariables().getLocalVariableMap();
		if(localVariableMap == null)
			return null;
		
		List<HistoryVariable> list = new ArrayList<HistoryVariable>(localVariableMap.size());
		localVariableMap.values().forEach(processVariable -> {
			list.add(new HistoryVariable(processDefinitionId, processInstanceId, historyTaskId, processVariable));
		});
		return list;
	}
	
	// 构建通用执行参数
	public FlowExecutionParameter buildFlowExecutionParameter() {
		List<Assignee> assignees = null;
		if(StringUtil.notEmpty(startParameter.getStartUserId())) {
			assignees = new ArrayList<Assignee>(1);
			assignees.add(new Assignee(startParameter.getStartUserId()));
		}
		return new FlowExecutionParameter(startParameter.getProcessVariables().getVariableMap(), assignees);
	}
}
