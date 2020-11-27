package com.douglei.bpm.module.runtime.instance.command;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.node.task.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ExecutionResult<ProcessInstance>> {
	private StartParameter parameter;
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;

	public ExecutionResult<ProcessInstance> execute() {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getStartingMode()) {
			case BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult<ProcessInstance>("启动失败, 不存在id为[%d]的流程定义", "bpm.process.start.id.unexists", parameter.getProcessDefinitionId());
				if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<ProcessInstance>("启动失败, id为[%d]的流程定义还未部署", "bpm.process.start.id.undeploy", parameter.getProcessDefinitionId());
				break;
			case BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult<ProcessInstance>("启动失败, 不存在code为[%s], version为[%s]的流程定义", "bpm.process.start.code.version.unexists", parameter.getCode(), parameter.getVersion());
				if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<ProcessInstance>("启动失败, code为[%s], version为[%s]的流程定义还未部署", "bpm.process.start.code.version.undeploy", parameter.getCode(), parameter.getVersion());
				break;
		}
		
		StartEventMetadata startEvent = processContainer.getProcess(processDefinition.getId()).getStartEvent();
		// TODO 使用开始节点启动流程 
		
		
		
		
		
		
		return null;
	}
}
