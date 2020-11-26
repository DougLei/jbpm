package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.components.command.context.transaction.SessionContext;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.node.Process;
import com.douglei.bpm.process.node.task.event.StartEvent;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ExecutionResult<ProcessRuntimeInstance>> {
	private StartParameter parameter;
	
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult<ProcessRuntimeInstance> execute(CommandContext commandContext) {
		ExecutionResult<Integer> processDefinitionId = getProcessDefinitionIdAfterValidate(commandContext.getSessionContext());
		if(processDefinitionId.isFail())
			return processDefinitionId.convertGenericsOnFail(ProcessRuntimeInstance.class);
		
		Process process = commandContext.getProcessHandler().getProcess(processDefinitionId.getObject());
		
		StartEvent startEvent = process.getStartEvent();
		
		// TODO 
		
		return null;
	}

	// 获取验证后的流程定义id
	private ExecutionResult<Integer> getProcessDefinitionIdAfterValidate(SessionContext sessionContext) {
		Object[] processDefinition;
		
		switch(parameter.getStartingMode()){
			case BY_PROCESS_DEFINITION_ID:
				processDefinition = sessionContext.getSqlSession().uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(parameter.getProcessDefinitionId()));
				if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.DELETE)
					return new ExecutionResult<Integer>("启动失败, 不存在id为[%d]的流程定义", "bpm.process.start.id.unexists", parameter.getProcessDefinitionId());
				if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<Integer>("启动失败, id为[%d]的流程定义还未发布", "bpm.process.start.id.undeploy", parameter.getProcessDefinitionId());
				return new ExecutionResult<Integer>(parameter.getProcessDefinitionId());
				
			case BY_PROCESS_DEFINITION_CODE_VERSION:
				processDefinition = sessionContext.getSqlSession().queryFirst_("select state, id from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(parameter.getCode(), parameter.getVersion()));
				if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.DELETE)
					return new ExecutionResult<Integer>("启动失败, 不存在code为[%s], version为[%s]的流程定义", "bpm.process.start.code.version.unexists", parameter.getCode(), parameter.getVersion());
				if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.UNDEPLOY)
					return new ExecutionResult<Integer>("启动失败, code为[%s], version为[%s]的流程定义还未发布", "bpm.process.start.code.version.undeploy", parameter.getCode(), parameter.getVersion());
				return new ExecutionResult<Integer>(Integer.parseInt(processDefinition[1].toString()));
		}
		throw new IllegalArgumentException("不支持使用["+parameter.getStartingMode()+"]方式启动流程");
	}
}
