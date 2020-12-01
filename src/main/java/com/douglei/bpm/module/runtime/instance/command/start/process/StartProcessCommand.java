package com.douglei.bpm.module.runtime.instance.command.start.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.ProcessObjectException;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.module.runtime.task.entity.Variable;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ProcessInstance> {
	private StartParameter parameter;
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;

	public ProcessInstance execute() {
		ProcessMetadata process = getProcess();
		ProcessInstance processInstance = createProcessInstance(process);
		
		StartEventMetadata startEvent = process.getStartEvent();
		
		
		
//		if(起始事件判断当前的启动条件不满足)
//			return new ExecutionResult<ProcessRuntimeInstance>("不能启动", "");
		
		
		
		// 创建流程运行任务
		Task processRuntimeTask = startEvent.createProcessRuntimeTask(process.getId(), processInstance.getId());
		SessionContext.getTableSession().save(processRuntimeTask);
		
		Assignee assignee = new Assignee();
		assignee.setTaskId(processRuntimeTask.getId());
		assignee.setMode(1);
		assignee.setUserId(parameter.getStartUserId());
		SessionContext.getTableSession().save(assignee);
		
		List<Variable> variables = new ArrayList<Variable>();
		parameter.getVariables().forEach((key, value) ->{
			Variable variable = new Variable();
			variable.setProcdefId(process.getId());
			variable.setProcinstId(processInstance.getId());
			variable.setTaskId(processRuntimeTask.getId());
			variable.setScope(1);
			variable.setName(key);
			variable.setDataType("string");
			variable.setStringVal(value.toString());
			
			variables.add(variable);
		});
		SessionContext.getTableSession().save(variables);
		
		
		
		return processInstance;
	}
	
	// 获取流程对象
	private ProcessMetadata getProcess() {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getStartingMode()) {
			case BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					throw new ProcessObjectException("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程");
				break;
			case BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					throw new ProcessObjectException("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
			throw new ProcessObjectException("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		return processContainer.getProcess(processDefinition.getId());
	}

	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata process) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(process.getId());
		instance.setTitle(process.getTitle(parameter.getVariables()));
		instance.setBusinessId(parameter.getBusinessId());
		instance.setPageId(process.getPageID());
		instance.setStartUserId(parameter.getStartUserId());
		instance.setStartTime(new Date());
		instance.setTenantId(parameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
}
