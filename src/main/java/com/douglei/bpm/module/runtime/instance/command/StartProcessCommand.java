package com.douglei.bpm.module.runtime.instance.command;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.State;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.scheduler.ProcessScheduler;
import com.douglei.bpm.process.scheduler.event.start.StartEventDispatchParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ExecutionResult>{
	private StartParameter startParameter;
	public StartProcessCommand(StartParameter startParameter) {
		this.startParameter = startParameter;
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private ProcessScheduler processScheduler;
	
	@Override
	public ExecutionResult execute() {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Start", startParameter);
		switch (startParameter.getMode()) {
			case StartParameter.BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在id为["+startParameter.getProcessDefinitionId()+"]的流程");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+startParameter.getCode()+"]的流程; 或未设置其主版本");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+startParameter.getCode()+"], version为["+startParameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getStateInstance() != State.DEPLOY)
			return new ExecutionResult("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		
		ProcessMetadata processMetadata = processContainer.getProcess(processDefinition.getId());
		return processScheduler.dispatchTask(processMetadata.getStartEvent(), new StartEventDispatchParameter(processMetadata, startParameter));
	}
}
