package com.douglei.bpm.module.runtime.instance.command;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.State;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.event.start.StartEventHandleParameter;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
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
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getMode()) {
			case StartParameter.BY_ID:
				if(processDefinition == null) 
					throw new TaskHandleException("启动失败, 不存在id为["+parameter.getId()+"]的流程");
				break;
			case StartParameter.BY_CODE:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为[%s]的流程; 或未设置其流程的主版本", "jbpm.process.start.fail.unset.major.version", parameter.getCode());
				break;
			case StartParameter.BY_CODE_VERSION:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为[%s], version为[%s]的流程", "jbpm.process.start.fail.code.version.unexists", parameter.getCode(), parameter.getVersion());
				break;
		}
		if(processDefinition.getStateInstance() != State.DEPLOY)
			return new ExecutionResult("启动失败, [%s]流程还未部署", "jbpm.process.start.fail.undeploy", processDefinition.getName());
		
		ProcessMetadata processMetadata = processEngineBeans.getProcessMappingContainer().getProcess(processDefinition.getId());
		return processEngineBeans.getTaskHandleUtil().startup(
				processMetadata.getStartEventMetadataEntity(),
				new StartEventHandleParameter(processMetadata, parameter, processEngineBeans.getUserBeanFactory()));
	}
}
