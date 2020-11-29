package com.douglei.bpm.module.runtime.instance.command;

import java.util.Date;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.node.task.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ExecutionResult<ProcessRuntimeInstance>> {
	private StartParameter parameter;
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;

	public ExecutionResult<ProcessRuntimeInstance> execute() {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getStartingMode()) {
			case BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult<ProcessRuntimeInstance>("启动失败, 不存在id为[%d]的流程定义", "bpm.process.start.id.unexists", parameter.getProcessDefinitionId());
				if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<ProcessRuntimeInstance>("启动失败, id为[%d]的流程定义还未部署", "bpm.process.start.id.undeploy", parameter.getProcessDefinitionId());
				break;
			case BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult<ProcessRuntimeInstance>("启动失败, 不存在code为[%s], version为[%s]的流程定义", "bpm.process.start.code.version.unexists", parameter.getCode(), parameter.getVersion());
				if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<ProcessRuntimeInstance>("启动失败, code为[%s], version为[%s]的流程定义还未部署", "bpm.process.start.code.version.undeploy", parameter.getCode(), parameter.getVersion());
				break;
		}
		
		ProcessMetadata process = processContainer.getProcess(processDefinition.getId());
		ProcessRuntimeInstance processRuntimeInstance = createProcessInstance(process);
		
		// TODO 给流程运行任务表插入数据
		StartEventMetadata startEvent = process.getStartEvent();
		System.out.println("刚刚启动了id为"+processRuntimeInstance.getId()+"流程实例");
		
		
		return new ExecutionResult<ProcessRuntimeInstance>(processRuntimeInstance);
	}

	// 创建流程实例
	private ProcessRuntimeInstance createProcessInstance(ProcessMetadata process) {
		ProcessRuntimeInstance instance = new ProcessRuntimeInstance();
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
