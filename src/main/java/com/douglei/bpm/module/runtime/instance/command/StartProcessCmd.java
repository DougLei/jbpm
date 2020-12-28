package com.douglei.bpm.module.runtime.instance.command;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.State;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.event.start.StartEventHandleParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.entity.StartEventMetadataEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class StartProcessCmd implements Command {
	private StartParameter parameter;
	public StartProcessCmd(StartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public ExecutionResult execute(BeanInstances beanInstances) {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getMode()) {
			case StartParameter.BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"]的流程; 或未设置其流程的主版本");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getStateInstance() != State.DEPLOY)
			return new ExecutionResult("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		
		ProcessMetadata processMetadata = beanInstances.getProcessContainer().getProcess(processDefinition.getId());
		StartEventMetadataEntity entity = processMetadata.getStartEvent();
		return beanInstances.getTaskHandlerUtil()
				.startup(entity.getTaskMetadata(), new StartEventHandleParameter(beanInstances, processMetadata, parameter));
	}
}
