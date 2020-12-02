package com.douglei.bpm.process.executor.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.component.variable.Scope;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.task.entity.Variable;
import com.douglei.bpm.process.executor.ProcessExecutionParameter;

/**
 * 
 * @author DougLei
 */
public class StartEventExecutionParameter implements ProcessExecutionParameter {
	private int processDefinitionId;
	private int processInstanceId;
	private StartParameter startParameter;
	
	public StartEventExecutionParameter(StartParameter startParameter) {
//		this.processDefinitionId = processDefinitionId;
//		this.processInstanceId = processInstanceId;
		this.startParameter = startParameter;
	}

	public int getProcessDefinitionId() {
		return processDefinitionId;
	}
	public int getProcessInstanceId() {
		return processInstanceId;
	}
	public List<Variable> getVariables(){
		if(startParameter.getVariables() != null) {
			List<Variable> variables = new ArrayList<Variable>(startParameter.getVariables().size());
			startParameter.getVariables().forEach((key, value) ->{
				variables.add(new Variable(processDefinitionId, processInstanceId, Scope.GLOBAL.getValue(), key, "string", value.toString()));
			});
			return variables;
		}
		return null;
	}
	public Map<String, Object> getVariableMap(){
		return startParameter.getVariables();
	}
}
