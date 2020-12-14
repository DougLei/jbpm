package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.variable.entity.Variable;
import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.bpm.process.handler.TaskDispatchParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class StartEventExecuteParameter implements ExecuteParameter {
	private ProcessMetadata processMetadata;
	private StartParameter startParameter;
	
	public StartEventExecuteParameter(ProcessMetadata processMetadata, StartParameter startParameter) {
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
	
	// 获取运行变量集合
	public List<Variable> getRuntimeVariables(int processDefinitionId, int processInstanceId){
		if(!startParameter.getProcessVariableMapHolder().existsGlobalVariableMap()) 
			return null;
		
		List<Variable> list = new ArrayList<Variable>(startParameter.getProcessVariableMapHolder().getGlobalVariableMap().size());
		startParameter.getProcessVariableMapHolder().getGlobalVariableMap().values().forEach(processVariable -> {
			list.add(new Variable(processDefinitionId, processInstanceId, null, processVariable));
		});
		return list;
	}
	
	// 获取历史变量集合
	public List<HistoryVariable> getHistoryVariables(int processDefinitionId, int processInstanceId, int historyTaskId){
		if(!startParameter.getProcessVariableMapHolder().existsLocalVariableMap()) 
			return null;
		
		List<HistoryVariable> list = new ArrayList<HistoryVariable>(startParameter.getProcessVariableMapHolder().getLocalVariableMap().size());
		startParameter.getProcessVariableMapHolder().getLocalVariableMap().values().forEach(processVariable -> {
			list.add(new HistoryVariable(processDefinitionId, processInstanceId, historyTaskId, processVariable));
		});
		return list;
	}
	
	// 构建调度参数
	public TaskDispatchParameter buildDispatchParameter(int procinstId) {
		List<Assignee> assignees = null;
		if(StringUtil.notEmpty(startParameter.getStartUserId())) {
			assignees = new ArrayList<Assignee>(1);
			assignees.add(new Assignee(startParameter.getStartUserId()));
		}
		return new TaskDispatchParameter(processMetadata.getId(), procinstId, assignees, startParameter.getProcessVariableMapHolder().getVariableMap());
	}
}
